package com.kosa.tm.domain.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 커스텀 레포지토리 임플
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-20         최혜령          최초 생성
 * 2024-08-20         최혜령          getLastDealProducts
 */
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final EntityManager em;

    /*
    ProductSelling.deadline == today이고, 현재 시간 이후인 경우의 Product 엔티티들을 추출
    (Product와 ProductSelling 엔티티는 OneToOne 관계
    우선순위 1 : Member.getRole() == '기업'
    우선순위 2 : Member.getRole() == '개인' and (Float) Member.getUserTemp 가 높을수록
     */
    @Override
    public List<Product> getLastDealProducts() {
        // 현재 날짜와 시간
        LocalDateTime now = LocalDateTime.now();

        // 오늘 날짜의 시작과 끝을 LocalDateTime으로 변환
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);

        // JPQL 쿼리 작성
        String jpql = "SELECT p " +
                "FROM Product p " +
                "JOIN p.member m " +
                "WHERE p.closeTime BETWEEN :startOfDay AND :endOfDay AND p.closeTime > :now AND p.quantity > 0" +
                "ORDER BY " +
                "CASE WHEN m.role = '기업' THEN 1 ELSE 2 END ASC, " +
                "CASE WHEN m.role = '개인' THEN m.memberTemp END DESC";

        // TypedQuery 생성 및 매개변수 설정
        TypedQuery<Product> query = this.em.createQuery(jpql, Product.class);
        query.setParameter("startOfDay", startOfDay);  // 오늘 날짜의 시작 시간
        query.setParameter("endOfDay", endOfDay);      // 오늘 날짜의 끝 시간
        query.setParameter("now", now);                // 현재 시간

        // 결과 리스트 반환
        return query.getResultList();
    }



    /*
    ProductSelling.startline == today 이지만, 시간이 현재시간 이후인 경우의 Product 엔티티들을 추출
    (Product와 ProductSelling 엔티티는 OneToOne 관계
    우선순위 1 : Member.getRole() == '기업'
    우선순위 2 : Member.getRole() == '개인' and (Float) Member.getUserTemp 가 높을수록
     */
    @Override
    public List<Product> getUpcomingProducts() {
        // 현재 날짜와 시간
        LocalDateTime now = LocalDateTime.now();

        // JPQL 쿼리 작성
        String jpql = "SELECT p " +
                "FROM Product p " +
                "JOIN p.member m " +
                "WHERE p.openTime > :now " +
                "ORDER BY " +
                "CASE WHEN m.role = '기업' THEN 1 ELSE 2 END ASC, " +
                "CASE WHEN m.role = '개인' THEN m.memberTemp END DESC";

        // TypedQuery 생성 및 매개변수 설정
        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("now", now);  // 현재 시간 설정

        // 결과 리스트 반환
        return query.getResultList();
    }


    /*
    ProductSelling.startline 이 현재 날짜및 시간기준 이전이며 deadline이 현재 날짜 및 시간기준 이후
    (Product와 ProductSelling 엔티티는 OneToOne 관계
    우선순위 1 : Member.getRole() == '기업'
    우선순위 2 : Member.getRole() == '개인' and (Float) Member.getUserTemp 가 높을수록
     */

    @Override
    public List<Product> getOngoingProducts() {
        // 현재 날짜와 시간
        LocalDateTime now = LocalDateTime.now();

        // JPQL 쿼리 작성
        String jpql = "SELECT p " +
                "FROM Product p " +
                "JOIN p.member m " +
                "WHERE p.openTime <= :now AND p.closeTime > :now AND p.quantity > 0" +
                "ORDER BY " +
                "CASE WHEN m.role = '기업' THEN 1 ELSE 2 END ASC, " +
                "CASE WHEN m.role = '개인' THEN m.memberTemp END DESC";

        // TypedQuery 생성 및 매개변수 설정
        TypedQuery<Product> query = em.createQuery(jpql, Product.class);
        query.setParameter("now", now);  // 현재 시간 설정

        // 결과 리스트 반환
        return query.getResultList();
    }





}
