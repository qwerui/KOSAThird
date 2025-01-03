package com.kosa.tm.domain.product;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductDto {
    private String title;
    private String uniqueImg;
    private float price;
    private String description;
    private int quantity;
    private String isUsed;
    private String sellingType;
    private LocalDateTime closeTime;
    private LocalDateTime openTime;
    private String isDirect;
    private Long shippingCost;
    private Long memberId; // memberId 추가
}
