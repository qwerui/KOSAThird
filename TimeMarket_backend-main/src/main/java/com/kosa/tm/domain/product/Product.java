package com.kosa.tm.domain.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kosa.tm.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 엔티티
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
@Getter
@Setter
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "UNIQUE_IMG")
    private String uniqueImg;

    @Column(name = "PRICE")
    private float price;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column
    private String isUsed;

    @Column
    private String sellingType;

    @Column
    private LocalDateTime closeTime;

    @Column
    private LocalDateTime openTime;

    @Column
    private String isDirect;

    @Column
    private Long shippingCost;

    @Column
    private Long bidUnit;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
