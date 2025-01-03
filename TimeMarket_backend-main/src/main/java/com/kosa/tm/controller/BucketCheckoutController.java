package com.kosa.tm.controller;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.product.ProductBuffer;
import com.kosa.tm.domain.want.BucketDTO;
import com.kosa.tm.domain.want.Want;
import com.kosa.tm.domain.want.WantService;
import lombok.RequiredArgsConstructor;
import org.postgresql.copy.CopyOut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class BucketCheckoutController {
    private final MemberRepository memberRepository;
    private final WantService wantService;

    @GetMapping("/api/bucket/{memberEmail}")
    public List<ProductBuffer> getBucket(@PathVariable("memberEmail") String memberEmail) {
        System.out.println("BUCKET : " + memberEmail);
        Member currentMember = memberRepository.findByEmail(memberEmail);
        if (currentMember == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found");
        }
        List<Want> wantList = wantService.getWantsByMember(currentMember);
        LocalDateTime now = LocalDateTime.now();

        List<ProductBuffer> products = new ArrayList<>();
        for (Want want : wantList) {
            if(want.getProduct().getSellingType().equals("경매")){
                ProductBuffer productBuffer = new ProductBuffer();
                productBuffer.setProduct(want.getProduct());
                products.add(productBuffer);
            }
            else if (want.getProduct().getCloseTime().isAfter(now) && want.getProduct().getQuantity() > 0) {
                ProductBuffer productBuffer = new ProductBuffer();
                productBuffer.setProduct(want.getProduct());
                products.add(productBuffer);
            }
        }

        System.out.println("size: " + products.size());

        return products;

    }

    @PostMapping("/api/bucket/{productId}")
    public ResponseEntity<?> deleteWantsByProductId(@PathVariable Long productId) {
        System.out.println("가자가자가자고 장바구니 지워보자고ㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗㅗ"  + productId);
        try {
            boolean isDeleted = wantService.deleteWantsByProductID(productId);
            System.out.println("isdeleted ::::::::::::::::::::" + isDeleted);

            if (isDeleted) {
                System.out.println("deleteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No related wants found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting wants");
        }
    }
}
