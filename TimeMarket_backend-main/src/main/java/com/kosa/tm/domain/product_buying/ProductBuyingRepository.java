package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 구매 상세 레포지토리
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
public interface ProductBuyingRepository extends JpaRepository<ProductBuying, Long> {
    List<ProductBuying> findByMember(Member member);
    List<ProductBuying> findByProduct(Product product);
    ProductBuying findById(long id);
    Optional<ProductBuying> findProductBuyingById(long id);
    ProductBuying save(ProductBuying productBuying);

    @Query("SELECT pb.product.id AS productId1, pb.state, pb.id, pb.member.name, pb.purchaseTime, pb.address, pb.buyingMemo, pb.member.email " +
            "FROM ProductBuying pb " +
            "JOIN pb.product p " +
            "JOIN p.member m " +
            "WHERE m.email = :email")
    List<Object[]> findProductsAndBuyingStatesByMemberEmail(String email);
}
