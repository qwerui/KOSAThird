package com.kosa.tm.domain.image;

import com.kosa.tm.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;
    @Override
    public Image saveImage(Product product, String url){
        Image image = new Image();
        image.setProduct(product);
        image.setImageUrl(url);
        return imageRepository.save(image);
    }
    @Override
    public Boolean deleteImage(Product product, Image img){
        List<Image> images = imageRepository.findAllByProduct(product);
        boolean exist = false;
        for(Image i : images){
            if (i.equals(img)){
                exist = true;
            }
        }
        if(exist){
            imageRepository.delete(img);
        }
        return exist;
    }

    @Override
    public List<Image> getAllImagesByProduct(Product product){
        return imageRepository.findAllByProduct(product);
    }

    /*
    전체 사진 배열을 받아서,
    삭제할건 삭제하고 신규 저장할건 신규저장함.
     */
    @Override
    public Boolean updateImages(Product product, List<String> imageUrls){
        List<Image> images = imageRepository.findAllByProduct(product);
        if(images.size()>10){
            return false;
        } else {
            for(String imageUrl : imageUrls){
                boolean found = false;
                for (Image image : images){
                    if(image.getImageUrl().equals(imageUrl)){
                        found = true;
                    }
                }
                if(!found){
                    Image image = new Image();
                    image.setProduct(product);
                    image.setImageUrl(imageUrl);
                    imageRepository.save(image);
                }
            }
            Map<String, Image> currentUrls = new HashMap<>();
            for(Image image : images){
                currentUrls.put(image.getImageUrl(), image);
            }
            for(String currentUrl : currentUrls.keySet()){
                if (!imageUrls.contains(currentUrl)){
                    imageRepository.delete(currentUrls.get(currentUrl));
                }
            }
            return true;
        }

    }
}
