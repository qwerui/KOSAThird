package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.MemberBuffer;
import com.kosa.tm.domain.product.ProductBuffer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAndBuyingDTO {
    private ProductBuffer product;
    private ProductBuyingBuffer productBuying;
    public ProductAndBuyingDTO() {
        product = new ProductBuffer();
        productBuying = new ProductBuyingBuffer();
    }
    public void setProductAndBuyingDTO (ProductBuffer productBuffer, ProductBuyingBuffer productBuyingByffer){

        this.product = productBuffer;
        this.productBuying = productBuyingByffer;
    }
}
