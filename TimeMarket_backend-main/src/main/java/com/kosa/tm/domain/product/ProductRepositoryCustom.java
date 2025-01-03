package com.kosa.tm.domain.product;

import java.util.List;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 커스텀 레포지토리
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
public interface ProductRepositoryCustom {
    List<Product> getLastDealProducts();
    List<Product> getUpcomingProducts();
    List<Product> getOngoingProducts();
}
