package com.kosa.tm.domain.auction;

import com.kosa.tm.domain.product.Product;

import java.util.List;

public interface AuctionService {
    List<Auction> getAuctionsByProductId(Long productId);

    Auction join(String memberId, Long productId, float price);

    Auction findBuyerInfoByAuctionProduct(Long productId);
    AuctionDTO findBuyerInfoDTO(Long productId);
}
