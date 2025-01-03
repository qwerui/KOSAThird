package com.kosa.tm.domain.alarm;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * author : 오재혁
 * date : 2024-08-20
 * description : 상품 알람 엔티티
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         오재혁           1차 완성
 */



@Entity
@Getter
@Setter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @Column(name = "title", nullable = false)
    private String title; // {productTitle} 제품이 오픈되었습니다!

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product")
    private Product product;

    @Column(name = "time")
    private LocalDateTime time;

    // alarm 생성되면 기본 false (알람 안읽음) ,  알람 체크하면 true로 변경
    @Column(name ="isread", nullable = false)
    private boolean isread = false;

    @Column(name ="type")
    private String type; // 구매 / 판매

}