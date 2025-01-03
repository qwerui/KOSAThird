package com.kosa.tm.domain.product;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberService;

import com.kosa.tm.domain.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*

@SpringBootTest
//@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private MemberService memberService;


    private Member member;
    private Product product;


    @BeforeEach
    public void setUp_withBasicProduct() {
        product = new Product();
        member = new Member();
        member.setMemberName("최혜령");
        member.setEmail("ellie19981008@gmail.com");
        member.setMemberPassword("1234");
        memberService.saveMember(member);

        product.setMember(member);
        product.setPrice(0);
        product.setTitle("무료나눔");
        product.setQuantity(1);
        product.setDescription("무료나눔 합니다.");
        product.setUniqueImg("/imag.png");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-20 23:00:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-21 16:30:00", formatter);
        product.setOpenTime(startline);
        product.setCloseTime(deadline);
        product.setSellingType("무료나눔");

        product.setIsDirect("T");
        product.setIsUsed("F");

        productService.saveProduct(product);
    }

    @Test
    void getAllProductByMemberIdTest(){
        assertEquals(1, productService.getAllProductByMemberId(member).size());
    }

    @Test
    void getAllUpcomingProductByMemberIdTest(){
        assertEquals(1, productService.getUpcomingProductByMemberId(member).size());
    }

    @Test
    void getLastDealProductByMemberIdTest(){
        assertEquals(0, productService.getLastDealProductByMemberId(member).size());
    }

    @ Test
    void getAllLastDealsTest1_validCase(){
        Product anotherProduct = new Product();
        anotherProduct.setMember(member);
        anotherProduct.setPrice(0);
        anotherProduct.setTitle("무료나눔");
        anotherProduct.setQuantity(1);
        anotherProduct.setDescription("무료나눔 합니다.");
        anotherProduct.setUniqueImg("/imag.png");



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-20 16:00:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-20 16:30:00", formatter);
        anotherProduct.setOpenTime(startline);
        anotherProduct.setCloseTime(deadline);
        anotherProduct.setSellingType("무료나눔");
        anotherProduct.setIsDirect("T");
        anotherProduct.setIsUsed("T");

        productService.saveProduct(anotherProduct);
        assertEquals(1, productService.getAllLastDeals().size());
    }

    @ Test
    void getAllLastDealsTest2_시간지남(){
        Product anotherProduct = new Product();
        anotherProduct.setMember(member);
        anotherProduct.setPrice(0);
        anotherProduct.setTitle("무료나눔");
        anotherProduct.setQuantity(1);
        anotherProduct.setDescription("무료나눔 합니다.");
        anotherProduct.setUniqueImg("/imag.png");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-21 01:00:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-21 13:30:00", formatter);
        anotherProduct.setOpenTime(startline);
        anotherProduct.setCloseTime(deadline);
        anotherProduct.setSellingType("무료나눔");
        anotherProduct.setIsDirect("T");
        anotherProduct.setIsUsed("T");

        productService.saveProduct(anotherProduct);
        assertEquals(0, productService.getAllLastDeals().size());
    }

    @ Test
    void getAllUpcomingDeals(){
        Product anotherProduct = new Product();
        anotherProduct.setMember(member);
        anotherProduct.setPrice(0);
        anotherProduct.setTitle("무료나눔");
        anotherProduct.setQuantity(1);
        anotherProduct.setDescription("무료나눔 합니다.");
        anotherProduct.setUniqueImg("/imag.png");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-23 17:05:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-24 23:30:00", formatter);
        anotherProduct.setOpenTime(startline);
        anotherProduct.setCloseTime(deadline);
        anotherProduct.setSellingType("무료나눔");
        anotherProduct.setIsDirect("T");
        anotherProduct.setIsUsed("T");

        productService.saveProduct(anotherProduct);
        assertEquals(1, productService.getAllUpcomingDeals().size());
    }

    @ Test
    void getAllLastDeals2(){
        Product anotherProduct = new Product();
        anotherProduct.setMember(member);
        anotherProduct.setPrice(0);
        anotherProduct.setTitle("무료나눔");
        anotherProduct.setQuantity(1);
        anotherProduct.setDescription("무료나눔 합니다.");
        anotherProduct.setUniqueImg("/imag.png");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-19 12:00:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-20 23:30:00", formatter);
        anotherProduct.setOpenTime(startline);
        anotherProduct.setCloseTime(deadline);
        anotherProduct.setSellingType("무료나눔");
        anotherProduct.setIsDirect("T");
        anotherProduct.setIsUsed("T");

        productService.saveProduct(anotherProduct);
        assertEquals(1, productService.getAllLastDeals().size());
    }

    @ Test
    void getOngoingDealsTest(){
        Product anotherProduct = new Product();
        anotherProduct.setMember(member);
        anotherProduct.setPrice(0);
        anotherProduct.setTitle("무료나눔");
        anotherProduct.setQuantity(1);
        anotherProduct.setDescription("무료나눔 합니다.");
        anotherProduct.setUniqueImg("/imag.png");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-20 12:00:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-26 13:30:00", formatter);
        anotherProduct.setOpenTime(startline);
        anotherProduct.setCloseTime(deadline);
        anotherProduct.setSellingType("무료나눔");
        anotherProduct.setIsDirect("T");
        anotherProduct.setIsUsed("F");

        productService.saveProduct(anotherProduct);
        assertEquals(1, productService.getAllOngoingDeals().size());
        assertEquals(1, productService.getOngoingProductByMemberId(member).size());
    }

    @Test
    void updateTest(){
        product.setQuantity(0);
        Product product1 = productService.updateProduct(product);
        assertEquals(product, product1);
    }

    @Test
    void getProductByKeywordTest() {
        Product anotherProduct = new Product();
        anotherProduct.setMember(member);
        anotherProduct.setPrice(0);
        anotherProduct.setTitle("무료나눔");
        anotherProduct.setQuantity(1);
        anotherProduct.setDescription("무료나눔 합니다.");
        anotherProduct.setUniqueImg("/imag.png");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startline = LocalDateTime.parse("2024-08-20 16:00:00", formatter);
        LocalDateTime deadline = LocalDateTime.parse("2024-08-29 16:30:00", formatter);
        anotherProduct.setOpenTime(startline);
        anotherProduct.setCloseTime(deadline);
        anotherProduct.setSellingType("무료나눔");
        anotherProduct.setIsDirect("T");
        anotherProduct.setIsUsed("T");

        var result = productService.getProductsByKeyword(0, "close", "나눔");
        assertTrue(result.getTotalElements() > 0);
    }
}
*/
