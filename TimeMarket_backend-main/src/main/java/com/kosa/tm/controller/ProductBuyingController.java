package com.kosa.tm.controller;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberBuffer;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductBuffer;
import com.kosa.tm.domain.product.ProductService;
import com.kosa.tm.domain.product_buying.*;

import com.kosa.tm.domain.product_buying.ProductBuying;
import com.kosa.tm.domain.product_buying.ProductBuyingDto;
import com.kosa.tm.domain.product_buying.ProductBuyingRepository;
import com.kosa.tm.domain.product_buying.ProductBuyingService;

import com.kosa.tm.domain.want.Want;
import com.kosa.tm.domain.want.WantRepository;
import com.kosa.tm.domain.want.WantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProductBuyingController {
    @Autowired
    private ProductBuyingService productBuyingService;
    @Autowired
    private ProductBuyingRepository productBuyingRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WantRepository wantRepository;
    @Autowired
    private WantService wantService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/api/submitProductBuy")
    public ResponseEntity<String> addProductBuying(@RequestBody ProductBuyingDto productBuyingDto) {
        Member member = memberRepository.findByEmail(productBuyingDto.getEmail());
        if (member == null) {
            return ResponseEntity.badRequest().body("Member not found");
        }

        Product product = productService.getProduct(productBuyingDto.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        ProductBuying productBuying = new ProductBuying();
        productBuying.setMember(member);
        productBuying.setPurchasePrice(productBuyingDto.getPurchasePrice());
        productBuying.setPurchaseTime(productBuyingDto.getPurchaseTime());
        productBuying.setAddress(productBuyingDto.getAddress());
        productBuying.setBuyingMemo(productBuyingDto.getBuyingMemo());
        productBuying.setQuantity(productBuyingDto.getQuantity());

        productBuyingService.buy(member, product, productBuying);

        List<Want> wants = wantService.getWantsByMember(member);
        Want wanted = null;
        for(Want want : wants) {
            if(want.getProduct().getId() == product.getId()) {
                wanted = want;
            }
        }
        wantService.deleteWant(wanted.getWant_id());
        return ResponseEntity.ok("상품이 성공적으로 등록되었습니다");
    }


    @PostMapping("/api/productBuyDetails")
    public List<Object[]> getProductBuyDetails(@RequestParam String memberEmail) {
        System.out.println("넘어온 productBuying의 email은 "+memberEmail);
        List<Object[]> productsAndBuyingStatesByMemberEmail
                = productBuyingService.getProductsAndBuyingStatesByMemberEmail(memberEmail);
        System.out.println("size는 "+productsAndBuyingStatesByMemberEmail.size());

        //확인용 출력
        for (Object[] objects : productsAndBuyingStatesByMemberEmail) {
            System.out.println("Objects array length길이는 " + objects.length);
            for (Object obj : objects) {
                if (obj != null) {
                    System.out.println("Class는 " + obj.getClass().getName());
                    System.out.println("Value는 " + obj.toString());
                } else {
                    System.out.println("Null object in array");
                }
            }
        }

        if(productsAndBuyingStatesByMemberEmail.size()>=0){
            return productsAndBuyingStatesByMemberEmail;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
    }

    @GetMapping("/api/productBuyDetailsbyBuyer/{memberEmail}")
    public Map<LocalDate, List<ProductAndBuyingDTO>> getProductBuyDetailsforBuyer(@PathVariable String memberEmail) {
        System.out.println("HELLLLLLLLLLLLLOOOOOOOOOOOOOOOOO");
        Member member = memberRepository.findByEmail(memberEmail);

        List<ProductBuying> productBuyingList = productBuyingService.getProductBuyingByMember(member);

        Map<LocalDate, List<ProductAndBuyingDTO>> productAndBuyingDTOMap = new HashMap<>();

        for(ProductBuying productBuying : productBuyingList) {
            LocalDate date = productBuying.getPurchaseTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            ProductBuffer productBuffer = new ProductBuffer();
            productBuffer.setProduct(productBuying.getProduct());
            Member seller = memberRepository.findByEmail(productService.getProduct(productBuying.getProduct().getId()).get().getMember().getEmail());
            MemberBuffer sellerBuffer = new MemberBuffer();
            sellerBuffer.setMember(seller);
            productBuffer.setMember(sellerBuffer);

            ProductBuyingBuffer productBuyingBuffer = new ProductBuyingBuffer();
            productBuyingBuffer.setProductBuyingBuffer(productBuying);

            ProductAndBuyingDTO productAndBuyingDTO = new ProductAndBuyingDTO();
            productAndBuyingDTO.setProductAndBuyingDTO(productBuffer, productBuyingBuffer);

            if(productAndBuyingDTOMap.containsKey(date)) {
                List<ProductAndBuyingDTO> originalList = productAndBuyingDTOMap.get(date);
                originalList.add(productAndBuyingDTO);
                productAndBuyingDTOMap.put(date, originalList);
            } else {
                List<ProductAndBuyingDTO> originalList = new ArrayList<>();
                originalList.add(productAndBuyingDTO);
                productAndBuyingDTOMap.put(date, originalList);
            }
        }
        System.out.println("FINALLLLL: " + productAndBuyingDTOMap.size());
        return productAndBuyingDTOMap;
    }


    @GetMapping("/api/bucket/wallet/{memberEmail}")
    public Long getProductBuying(@PathVariable String memberEmail) {
        System.out.println("User Wallet: " + memberRepository.findByEmail(memberEmail).getWallet());
        return memberRepository.findByEmail(memberEmail).getWallet();
    }

    // 0829 최혜령
    @PostMapping("/api/productBuying/confirm/{id}")
    public ResponseEntity<String> confirmOrder(@PathVariable Long id){
        ProductBuying productBuying = productBuyingRepository.findById(id).orElse(null);
        if(productBuying == null) {
            return ResponseEntity.badRequest().build();
        }
        productBuying.setState(4);
        productBuyingRepository.save(productBuying);
        return ResponseEntity.ok().build();
    }


    //나선주
    @PostMapping("/api/updateProductStatusSelected")
    public ResponseEntity<Map<String, Object>> updateProductStatus(@RequestBody UpdateStatusDto request) {
        Integer pbId = request.getPbId();
        Integer newState = request.getNewState();
        System.out.println("넘어온 pbId는"+pbId);
        System.out.println("넘어온 상태는 "+newState);

        if (pbId == null || newState == null) {
            return new ResponseEntity<>(Map.of("error", "Invalid data"), HttpStatus.BAD_REQUEST);
        }

        // 상태 업데이트
        ProductBuying byId = productBuyingRepository.findProductBuyingById(pbId).get();
        byId.setState(newState);
        productBuyingRepository.save(byId);

        // 성공적인 응답
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Product status updated successfully");
        response.put("productId", pbId);
        response.put("newState", newState);

        return ResponseEntity.ok(response);
    }

    //재혁이
    @PostMapping("/api/usePoints")
    public ResponseEntity<String> usePoints(@RequestParam String memberEmail, @RequestParam int pointsToUse) {
        System.out.println("포인트소모하도록하겟습니다ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ ::" + memberEmail + "  " + pointsToUse);
        // 멤버를 이메일로 조회
        Member member = memberRepository.findByEmail(memberEmail);
        if (member == null) {
            return ResponseEntity.badRequest().body("Member not found");
        }

        // 포인트 소모 처리
        if (member.getWallet() >= pointsToUse) {
            // 사용 가능한 포인트가 있는지 확인
            member.setWallet(member.getWallet() - pointsToUse); // 포인트 차감
            memberRepository.save(member); // 업데이트된 멤버 저장
            return ResponseEntity.ok("포인트 사용이 성공적으로 완료되었습니다.");

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트 사용에 실패했습니다.");
        }
    }

}
