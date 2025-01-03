package com.kosa.tm.domain.image;

import com.kosa.tm.domain.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ImageService {
    Image saveImage(Product product, String url);

    Boolean deleteImage(Product product, Image img);

    List<Image> getAllImagesByProduct(Product product);

    Boolean updateImages(Product product, List<String> imageUrls);

}
