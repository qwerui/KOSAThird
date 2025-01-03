package com.kosa.tm.domain.auction;

import com.kosa.tm.domain.image.Image;
import com.kosa.tm.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findAllByProduct(Product product);

}
