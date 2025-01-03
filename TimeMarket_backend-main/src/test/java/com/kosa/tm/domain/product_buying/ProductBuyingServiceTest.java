package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductBuyingServiceTest {
    @Autowired
    private ProductBuyingService productBuyingService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductBuyingRepository productBuyingRepository;

    @Test
    void buytests() throws ParseException {
        Member member = memberService.findMemberById(14L).get();
        Product product = productService.getProduct(20L).get();
        System.out.println("member는 " + member);
        System.out.println("product는 " + product);

        ProductBuying productBuying = new ProductBuying();
        productBuying.setPurchasePrice(1000);
        productBuying.setQuantity(0);

        // SimpleDateFormat을 사용하여 String을 Date로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date purchaseTime = dateFormat.parse("2024-08-28 12:00:00");
        productBuying.setPurchaseTime(purchaseTime); // Date 타입의 purchaseTime 설정

        productBuying.setState(1);
        productBuying.setAddress("부산광역시 어디구 어느동 어느아파트");
        productBuying.setMember(member);
        productBuying.setProduct(product);
        productBuying.setBuyingMemo("경비실에 둬주세요");

        productBuyingRepository.save(productBuying);
        List<ProductBuying> productBuyingByProductId = productBuyingService.getProductBuyingByProductId(product);
        System.out.println("size는"+productBuyingByProductId.size());

        assertNotNull(productBuyingService.getProductBuyingByProductId(product));
    }
}