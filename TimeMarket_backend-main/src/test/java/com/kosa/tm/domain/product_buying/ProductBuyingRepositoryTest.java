package com.kosa.tm.domain.product_buying;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductBuyingRepositoryTest {
    @Autowired
    private ProductBuyingRepository productBuyingRepository;

    @Test
    @Transactional
    void testfindProductBuyingById() {
        long id =41L;
        productBuyingRepository.findProductBuyingById(id);
        System.out.println("안녕"+productBuyingRepository.findProductBuyingById(id));
        assertNotNull(productBuyingRepository.findProductBuyingById(id));
    }
}