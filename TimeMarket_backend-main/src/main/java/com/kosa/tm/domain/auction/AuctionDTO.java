package com.kosa.tm.domain.auction;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionDTO {
    private String UserEmail;
    private Long product_id;
    private LocalDateTime join_time;
    private float price;
}
