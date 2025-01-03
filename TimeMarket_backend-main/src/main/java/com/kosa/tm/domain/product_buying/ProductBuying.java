package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 구매 상세 엔티티
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
@Entity
public class ProductBuying {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PB_ID")
    private Long id;

    @Column(name = "PURCHASE_PRICE")
    private float purchasePrice;

    @Column(name = "QUANTITY")
    private int quantity;

    @Column(name = "PURCHASE_TIME")
    private Date purchaseTime;

    @Column(name = "STATE")
    private Integer state;

    @Column(name = "ADDRESS")
    private String address;

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column
    private String buyingMemo;
}
