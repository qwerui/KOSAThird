package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;

import java.util.List;
/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 구매 상세 서비스 인터페이스
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
public interface ProductBuyingService {
    List<ProductBuying> getProductBuyingByMember(Member member);

    List<ProductBuying> getProductBuyingByProductId(Product product);

    List<ProductBuying> getProductBuyingByProductIdAndMember(Long productId, Member member);

    ProductBuying buy(Member member, Product product, ProductBuying productBuying);

    boolean cancel(Member member, Product product, ProductBuying productBuying);

    ProductBuying updateStatus(Product product, ProductBuying productBuying);

    //나선주
    ProductBuying completeBuying(ProductBuying productBuying);

    //나선주
    List<Object[]> getProductsAndBuyingStatesByMemberEmail(String email);
}
