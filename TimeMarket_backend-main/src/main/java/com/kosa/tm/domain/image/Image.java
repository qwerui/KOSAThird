package com.kosa.tm.domain.image;

import com.kosa.tm.domain.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 사진들 엔티티
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
@Getter
@Setter
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "IMAGE_URL")
    private String imageUrl;


}
