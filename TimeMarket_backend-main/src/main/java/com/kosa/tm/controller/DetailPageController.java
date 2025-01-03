package com.kosa.tm.controller;

import com.kosa.tm.domain.image.Image;
import com.kosa.tm.domain.image.ImageBuffer;
import com.kosa.tm.domain.image.ImageService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductBuffer;
import com.kosa.tm.domain.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DetailPageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/api/images-all/{productId}")
    public List<ImageBuffer> getAllImagesByProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId).get();
        List<Image> images = imageService.getAllImagesByProduct(product);
        List<ImageBuffer> buffer = new ArrayList<>();
        for(var i : images){
            var buf = new ImageBuffer();
            buf.setImage(i);
            buffer.add(buf);
        }

        return buffer;
    }
}

