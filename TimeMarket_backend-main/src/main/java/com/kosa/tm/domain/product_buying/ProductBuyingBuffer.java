package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProductBuyingBuffer {
    private Long id;

    private float purchasePrice;

    private int quantity;

    private Date purchaseTime;

    private Integer state;

    private String address;

    private String buyingMemo;

    public void setProductBuyingBuffer(ProductBuying productBuying) {
        this.id = productBuying.getId();
        this.purchasePrice = productBuying.getPurchasePrice();
        this.quantity = productBuying.getQuantity();
        this.purchaseTime = productBuying.getPurchaseTime();
        this.state = productBuying.getState();
        this.address = productBuying.getAddress();
        this.buyingMemo = productBuying.getBuyingMemo();
    }
}
