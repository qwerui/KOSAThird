//package com.kosa.tm.domain.image;
//
//import com.kosa.tm.domain.member.Member;
//import com.kosa.tm.domain.member.MemberRepository;
//import com.kosa.tm.domain.member.MemberService;
//import com.kosa.tm.domain.product.Product;
//import com.kosa.tm.domain.product.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//@SpringBootTest
//@Transactional
//public class ImageServiceTest {
//    @Autowired
//    private ProductService productService;
//
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private MemberService memberService;
//
//
//    private Member member;
//    private Product product;
//
//    @BeforeEach
//    public void setUp() {
//        product = new Product();
//        member = new Member();
//
//        member.setMemberName("최혜령");
//        member.setEmail("ellie19981008@gmail.com");
//        member.setMemberPassword("1234");
//        memberService.saveMember(member);
//
//        product.setMember(member);
//        product.setPrice(0);
//        product.setTitle("무료나눔");
//        product.setQuantity(1);
//        product.setDescription("무료나눔 합니다.");
//        product.setUniqueImg("/imag.png");
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime startline = LocalDateTime.parse("2024-08-20 16:00:00", formatter);
//        LocalDateTime deadline = LocalDateTime.parse("2024-08-20 16:30:00", formatter);
//        productSelling.setStartline(startline);
//        productSelling.setDeadline(deadline);
//        productSelling.setSellingType((long)1);
//        productSelling.setDirectTradeFlag(true);
//        productSelling.setProduct_state((long)1);
//
//        productService.saveProduct(product);
//        productSellingService.createProductSelling(product, productSelling);
//    }
//
//    @Test
//
//    void addImageTest(){
//        Image image = imageService.saveImage(product, "/test.png");
//        assertEquals(image.getImageUrl(), "/test.png");
//    }
//    @Test
//    void deleteImageTest_success(){
//        Image image = imageService.saveImage(product, "/test.png");
//        Boolean result = imageService.deleteImage(product, image);
//        assertEquals(true, result);
//    }
//
//    @Test
//    void deleteImageTest_fail(){
//        Image invalidImage = new Image();
//        invalidImage.setImageUrl("/invalid.png");
//        invalidImage.setProduct(product);
//        Boolean result = imageService.deleteImage(product, invalidImage);
//        assertEquals(false, result);
//    }
//
//    @Test
//    void updateImageTest_success(){
//        Image image = imageService.saveImage(product, "/test.png");
//        List<String> images = new ArrayList<>();
//        images.add("/test1.png");
//        images.add("/test2.png");
//
//        imageService.updateImages(product, images);
//
//        List<Image> actualImgs = imageService.getAllImagesByProduct(product);
//
//        assertEquals(images.size(), actualImgs.size());
//
//        for(Image img : actualImgs){
//            assertFalse(img.getImageUrl().equals(image.getImageUrl()));
//        }
//    }
//}
