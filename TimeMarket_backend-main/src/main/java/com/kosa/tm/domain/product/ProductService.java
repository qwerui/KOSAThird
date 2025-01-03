package com.kosa.tm.domain.product;

import com.kosa.tm.domain.member.Member;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 상품 서비스 인터페이스
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 * 2024-08-22         홍제기           검색 결과 메소드 생성
 */
public interface ProductService {

    /*
    특정 유저의 현재 판매중인 기록
     */
    List<Product> getOngoingProductByMemberId(Member member);

    /*
        특정 유저의 오픈되지 않은 판매 기록
         */
    List<Product> getUpcomingProductByMemberId(Member member);

    List<Product> getAllLastDeals();

    //예전사용
    List<Product> getAllProductByMemberId(Member member);

    //productBuffer로 변경 - 나선주
    List<ProductBuffer> getAllfinalProductByMemberId(Member member);

    List<Product> getLastDealProductByMemberId(Member member);

    /*
    메인에 보여야 하는 모든 오픈 예정 상품들 (이미 정렬됨)
     */
    List<Product> getAllUpcomingDeals();

    /*
        메인에 보여야 하는 모든 현재 거래중인 상품들 (이미 정렬됨)
         */
    List<Product> getAllOngoingDeals();

    /*
        새 상품 저장
         */
    Product saveProduct(Product product);

    /*
        상품 삭제
         */
    boolean deleteProduct(Product product);

    /*
            상품 수정
             */
    Product updateProduct(Product product);

    //필요해서 만듬 - 나선주
    public Optional<Product> getProduct(Long productId);

    //검색 요청 - 홍제기
    public Page<Product> getProductsByKeyword(int page, String searchType, String keyword);
    public List<Product> getOngoingAuctions();
}
