package com.kosa.tm.domain.auction;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class AuctionServiceTest {

    @Autowired
    private AuctionService auctionService;

    @Test
    public void testGetBuyer(){
        var buyer = auctionService.findBuyerInfoByAuctionProduct(23L);
        assertNotNull(buyer.getMember());
        System.out.println("Buyer : "+buyer.getMember().getEmail());
    }
}
