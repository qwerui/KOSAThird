package com.kosa.tm.domain.want;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberBuffer;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductBuffer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BucketDTO {
    private ProductBuffer product;
    private MemberBuffer seller;

    public BucketDTO(){
        seller = new MemberBuffer();
        product = new ProductBuffer();
    }

    public void setBucket(Product product){
        this.product.setProduct(product);
        this.seller.setMember(product.getMember());
    }
}
