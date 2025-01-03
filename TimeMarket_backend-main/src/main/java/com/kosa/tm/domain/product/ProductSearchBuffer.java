package com.kosa.tm.domain.product;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ProductSearchBuffer {
    private Long id;
    private String title;
    private float price;
    private LocalDateTime closeTime;
}
