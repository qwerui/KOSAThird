package com.kosa.tm.domain.product_buying;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusDto {
    private int pbId;
    private int newState;
}
