package com.kosa.tm.domain.product;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberBuffer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductBuffer {
    private Long id;
    private String title;
    private String uniqueImg;
    private float price;
    private String description;
    private int quantity;
    private MemberBuffer member;
    private String isUsed;
    private String sellingType;
    private LocalDateTime closeTime;
    private LocalDateTime openTime;
    private String isDirect;
    private Long shippingCost;
    private Long bidUnit;

    public ProductBuffer(){
        member = new MemberBuffer();
    }

    public void setProduct(Product product){
        id = product.getId();
        title = product.getTitle();
        uniqueImg = product.getUniqueImg();
        price = product.getPrice();
        description = product.getDescription();
        quantity = product.getQuantity();
        member.setMember(product.getMember());
        isUsed = product.getIsUsed();
        sellingType = product.getSellingType();
        closeTime = product.getCloseTime();
        openTime = product.getOpenTime();
        isDirect = product.getIsDirect();
        shippingCost = product.getShippingCost();
        bidUnit = product.getBidUnit();
    }
}
