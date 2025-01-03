package com.kosa.tm.controller;

import com.kosa.tm.domain.alarm.WantAlarmScheduler;
import com.kosa.tm.domain.image.ImageService;
import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberBuffer;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductBuffer;
import com.kosa.tm.domain.product.ProductRepository;
import com.kosa.tm.domain.product.ProductService;
import com.kosa.tm.domain.product_buying.ProductBuying;
import com.kosa.tm.domain.product_buying.ProductBuyingRepository;
import com.kosa.tm.domain.want.Want;
import com.kosa.tm.domain.want.WantService;
import com.kosa.tm.googleCloud.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WantAlarmScheduler wantAlarmScheduler;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    GoogleCloudStorageService googleCloudStorageService;
    @Autowired
    private WantService wantService;
    @Autowired
    private ProductBuyingRepository productBuyingRepository;

    @PostMapping("/api/products")
    public ResponseEntity<String> addProduct(
            @RequestParam String title,
            @RequestParam MultipartFile[] files,
            @RequestParam Long price,
            @RequestParam String description,
            @RequestParam int quantity,
            @RequestParam String isUsed,
            @RequestParam String sellingType,
            @RequestParam String isDirect,
            @RequestParam Long shippingCost,
            @RequestParam String closeTime,
            @RequestParam String openTime,
            @RequestParam Long bidUnit,
            @RequestParam String memberId,
            Authentication authentication
            ) throws IOException {

        Product product = new Product();
        product.setTitle(title);
        product.setPrice(price);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setIsUsed(isUsed);
        product.setSellingType(sellingType);
        product.setIsDirect(isDirect);
        product.setShippingCost(shippingCost);
        product.setBidUnit(bidUnit);

        String closedateTimeString = closeTime;
        String opendateTimeString = openTime;
        int seconds = 0;
        System.out.println("closeTime은 "+closeTime);
        System.out.println("openTim은 "+openTime);

        // 문자열을 LocalDateTime으로 변환
        LocalDateTime closedateTime = LocalDateTime.parse(closedateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime opendateTime = LocalDateTime.parse(opendateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        closedateTime = closedateTime.withSecond(seconds);
        opendateTime = opendateTime.plusSeconds(seconds);
        System.out.println("closedateTime은 "+closedateTime);

        product.setCloseTime(closedateTime);
        product.setOpenTime(opendateTime);

        try {
            Member member = memberRepository.findByEmail(memberId); //memberid라고 돼있지만 email을 가져오는 중

            product.setMember(member); // Member 객체 설정

            System.out.println("파일의 길이는 "+files.length);

            String[] imageUrls = new String[files.length];

            for(int i=0; i<files.length; i++) {
                MultipartFile file = files[i];

                String uploadDir = new File("src/main/resources/static/images").getAbsolutePath();
                // 고유 파일명을 생성
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                try {
                    googleCloudStorageService.uploadFile(file, fileName);
                    System.out.println("file: " + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 파일 저장 경로 생성
                Path path = Paths.get(uploadDir + File.separator + fileName);
                // 파일을 지정된 경로에 저장
//                Files.write(path, file.getBytes());

                // 파일의 URL 경로를 설정 (예: /images/filename.jpg)
                System.out.println("================================================================");

                String fileUrl =  fileName;
                System.out.println("url은 "+fileUrl);
                if(i==0){
                    product.setUniqueImg(fileUrl); //대표이미지 설정
                }
                imageUrls[i]=fileUrl;

            }
            System.out.println("imageUrl들의 수는 "+imageUrls.length);

            Product savedProduct = productService.saveProduct(product);

            for(String url : imageUrls){
                imageService.saveImage(product, url);

            }

            if(savedProduct.getOpenTime().isBefore(LocalDateTime.now())){
                wantAlarmScheduler.addUpcomingProduct(savedProduct);
            }
        } catch (Exception e) {
            throw new RuntimeException("Member not found");
        }

        return ResponseEntity.ok("상품이 성공적으로 등록되었습니다");
    }


    @GetMapping("/api/payment/product/{productId}")
    public Product getProduct(@PathVariable Long productId){
        return productService.getProduct(productId).get();
    }


    @GetMapping("/api/search")
    public Page<Product> getProductsByKeyword(@RequestParam int page, @RequestParam String searchType, @RequestParam String keyword){
        // JPA의 Page는 인덱스가 0부터 시작하기 때문에 -1을 해야한다.
        return productService.getProductsByKeyword(page-1, searchType, keyword);
    }
  
    @PostMapping("/api/productList")
    public List<ProductBuffer> getProductListbyEmail(@RequestParam String memberEmail){
        System.out.println("넘어온 email은 "+memberEmail);
        Member member = memberRepository.findByEmail(memberEmail);

        if(member!=null){
            List<ProductBuffer> allfinalProductByMemberId = productService.getAllfinalProductByMemberId(member);
            return productService.getAllfinalProductByMemberId(member);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다");
        }
    }

    @PostMapping("/api/productListofBuyer")
    public List<ProductBuffer> getProductListbyBuyerEmail(@RequestParam String memberEmail){

        Member member = memberRepository.findByEmail(memberEmail);

        if(member!=null){
            MemberBuffer memberBuffer = new MemberBuffer();
            memberBuffer.setMember(member);
            List<ProductBuying> productBuyings = productBuyingRepository.findByMember(member);
            List<ProductBuffer> productBuffers = new ArrayList<ProductBuffer>();
            for(ProductBuying productBuying : productBuyings){
                ProductBuffer productBuffer = new ProductBuffer();
                productBuffer.setProduct(productBuying.getProduct());
                productBuffer.setMember(memberBuffer);
                productBuffers.add(productBuffer);
            }
            return productBuffers;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다");
        }
    }

    @PostMapping("/api/want/{productId}/{memberEmail}")
    public ResponseEntity<String> addBucket(@PathVariable Long productId, @PathVariable String memberEmail){
        Want want = new Want();
        Member member = memberRepository.findByEmail(memberEmail);
        Product product = productService.getProduct(productId).get();
        want.setMember(member);
        want.setProduct(product);
        List<Want> wantList = wantService.getAllWants();
        boolean canAdd = true;
        for (Want w : wantList){
            if (w.getMember() == member && w.getProduct() == product){
                canAdd = false;
            }
        }
        if(canAdd){
            Want wantResult = wantService.createWant(want);
            return ResponseEntity.ok("장바구니에 추가되었습니다.");
        }
        return ResponseEntity.ok("이미 장바구니에 추가되어 있습니다.");
    }
}
