package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductBuffer;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AlarmBuffer {
    private Long id;
    private String title;
    private String description;
    private ProductBuffer product;
    private LocalDateTime time;
    private String type;

    public AlarmBuffer(){
        product = new ProductBuffer();
    }

    public void setProduct(Product product){
        this.product.setProduct(product);
    }
}
