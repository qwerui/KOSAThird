package com.kosa.tm.domain.product_buying;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductBuyingDto {
    private String email;
    private float purchasePrice;
    private int quantity;
    private Date purchaseTime;
    private String address;
    private Long productId;
    private String buyingMemo;

    public void setProductBuyingDto (ProductBuying productBuying){
        this.email = productBuying.getMember().getEmail();
        this.purchasePrice = productBuying.getPurchasePrice();
        this.quantity = productBuying.getQuantity();
        this.purchaseTime = productBuying.getPurchaseTime();
        this.address = productBuying.getAddress();
        this.productId = productBuying.getId();
        this.buyingMemo = productBuying.getBuyingMemo();
    }
}
