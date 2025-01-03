package com.kosa.tm.domain.image;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageBuffer {
    private long id;
    private long productId;
    private String imageUrl;

    public void setImage(Image image){
        this.imageUrl = image.getImageUrl();
        this.productId = image.getProduct().getId();
        this.id = image.getId();
    }
}
