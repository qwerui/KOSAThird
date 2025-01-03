package com.kosa.tm;

import com.kosa.tm.domain.image.Image;
import com.kosa.tm.domain.image.ImageService;
import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/*

@SpringBootTest
class TmApplicationTests {
	@Autowired
	private ProductService productService;

	@Autowired
	private MemberService memberService;


	@Autowired
	private ImageService imageService;
	private Member member;


	@Test
	void contextLoads1_ongoingDeal() {
		Product product = new Product();
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
		LocalDateTime deadline = LocalDateTime.parse("2024-08-25 16:30:00", formatter);
        product.setOpenTime(startline);
        product.setCloseTime(deadline);
        product.setSellingType("무료나눔");
        product.setIsDirect("T");
        product.setIsUsed("T");
		productService.saveProduct(product);
	}

	@Test
	void contextLoads2_upcomingDeal() {
		Product product = new Product();
		Member member2 = new Member();

		member2.setMemberName("오재혁");
		member2.setEmail("jhuk@gmail.com");
		member2.setMemberPassword("1234");
		memberService.saveMember(member2);

		product.setMember(member2);
		product.setPrice(9900);
		product.setTitle("커피쿠폰");
		product.setQuantity(10);
		product.setDescription("재혁이네 커피 만원권");
		product.setUniqueImg("/imag2.png");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startline = LocalDateTime.parse("2024-08-21 23:00:00", formatter);
		LocalDateTime deadline = LocalDateTime.parse("2024-08-24 16:30:00", formatter);
        product.setOpenTime(startline);
        product.setCloseTime(deadline);
        product.setSellingType("선착순");
        product.setIsDirect("T");
        product.setIsUsed("F");

		productService.saveProduct(product);
	}

	@Test
	void contextLoads3_lastDeal() {
		Product product = new Product();
		Member member3 = new Member();

		member3.setMemberName("오재혁");
		member3.setEmail("jhuk@gmail.com");
		member3.setMemberPassword("1234");
		memberService.saveMember(member3);

		product.setMember(member3);
		product.setPrice(9900);
		product.setTitle("커피쿠폰");
		product.setQuantity(10);
		product.setDescription("재혁이네 커피 만원권");
		product.setUniqueImg("/imag2.png");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startline = LocalDateTime.parse("2024-08-20 23:00:00", formatter);
		LocalDateTime deadline = LocalDateTime.parse("2024-08-21 23:30:00", formatter);
        product.setOpenTime(startline);
        product.setCloseTime(deadline);
        product.setSellingType("경매");
        product.setIsDirect("T");
        product.setIsUsed("F");



		productService.saveProduct(product);
	}



	@Test
	void contextLoads4_재혁이네커피사진데이터입력() {

		List<Member> allMembers = memberService.findAllMembers();
		Member jh = null;
		for(Member m : allMembers){
			if (m.getRole().equals("기업")){
				jh = m;
			}
		}

		Product jhProduct = null;
		if(jh != null){
			List<Product> jh_products = productService.getAllProductByMemberId(jh);
			for(Product p : jh_products){
				jhProduct = p;
			}
		}

		// 새로운 사진 추가
		if(jhProduct != null){
			imageService.saveImage(jhProduct, "/coffee_jh.png");
			imageService.saveImage(jhProduct, "/imag.png");
		}

	}

}*/
