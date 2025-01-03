package com.kosa.tm.domain.product;

import com.kosa.tm.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 JPA 레포지토리
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 * 2024-08-22         홍제기           검색 페이지 결과 요청
 */
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    List<Product> findByMember(Member member);
    Page<Product> findAllByCloseTimeGreaterThanAndTitleContainingOrderByCloseTimeAsc(Pageable pageable, LocalDateTime closeTime, String title);
    Page<Product> findAllByOpenTimeGreaterThanAndTitleContainingOrderByOpenTimeAsc(Pageable pageable, LocalDateTime openTime, String title);


}
