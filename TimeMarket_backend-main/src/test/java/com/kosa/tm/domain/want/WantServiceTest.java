package com.kosa.tm.domain.want;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import com.kosa.tm.domain.product.ProductServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/*
@SpringBootTest
@Log4j2
class WantServiceTest {
   @Autowired
    WantService wantService;
    @Autowired
    MemberService memberService;
    @Autowired
    ProductServiceImpl productServiceImpl;


    @Test
    @Transactional
    void testgetWantsByMember() {
        Member member = new Member();
        member.setMemberName("최혜령");
        member.setEmail("ellie19981008@gmail.com");
        member.setMemberPassword("1234");
        memberService.saveMember(member);

        //product준비
        Product product = new Product();
        product.setDescription("물건test입니다");
        product.setPrice(4000);
        product.setQuantity(4);
        product.setTitle("test product 타이틀입니다");
        product.setUniqueImg("https://picsum.photos/id/4/200/300");
        productServiceImpl.saveProduct(product);

        //productselling준비

        LocalDateTime now2 = LocalDateTime.now();
        LocalDateTime futureTime = now2.plusMinutes(30);
        product.setCloseTime(futureTime);

        product.setIsDirect("T");
        product.setIsUsed("T");
        product.setShippingCost(3000L);
        product.setOpenTime(LocalDateTime.now());
        product.setSellingType("무료나눔");

        Want want = new Want();
        want.setMember(member);
        want.setProduct(product);
        wantService.createWant(want);

        log.info("값은 "+wantService.getWantsByMember(member).size());

        assertTrue(wantService.getWantsByMember(member).size()!=0);
    }

    @Test
    void testForAlarmToCreateWant() {
        Member member = memberService.findMemberById(1L).get();
        Product product = productServiceImpl.getProduct(98L).get();

        Want want = new Want();
        want.setMember(member);
        want.setProduct(product);

        wantService.createWant(want);
    }

}
*/
