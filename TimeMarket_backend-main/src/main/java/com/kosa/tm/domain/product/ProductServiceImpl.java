package com.kosa.tm.domain.product;

import com.kosa.tm.domain.auction.Auction;
import com.kosa.tm.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 서비스 구현클래스
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    /*
    특정 유저의 모든 판매 기록
     */
    @Override
    public List<Product> getAllProductByMemberId(Member member){
        return productRepository.findByMember(member);
    }

    @Override
    public List<ProductBuffer> getAllfinalProductByMemberId(Member member) {
        List<Product> byMember = productRepository.findByMember(member);
        
        List<ProductBuffer> buffers = new ArrayList<>();

        byMember.forEach(product -> {
            ProductBuffer buffer = new ProductBuffer();
            buffer.setProduct(product);
            System.out.println("buffer에 저장된 값은"+buffer.getTitle());
            buffers.add(buffer);
        });

        return buffers;
    }

    /*
    특정 유저의 마감임박 기록
     */
    @Override
    public List<Product> getLastDealProductByMemberId(Member member){
        List<Product> lastDeals = productRepository.getLastDealProducts();
        List<Product> myDeals = new ArrayList<>();
        for(Product product : lastDeals){
            if(product.getMember().getId().equals(member.getId())){
                myDeals.add(product);
            }
        }
        return myDeals;
    }

    /*
    특정 유저의 현재 판매중인 기록
     */
    @Override
    public List<Product> getOngoingProductByMemberId(Member member){
        List<Product> lastDeals = productRepository.getOngoingProducts();
        List<Product> myDeals = new ArrayList<>();
        for (Product product : lastDeals){
            if(product.getMember().getId().equals(member.getId())){
                myDeals.add(product);
            }
        }
        return myDeals;
    }

    /*
    특정 유저의 오픈되지 않은 판매 기록
     */
    @Override
    public List<Product> getUpcomingProductByMemberId(Member member){
        List<Product> lastDeals = productRepository.getUpcomingProducts();
        List<Product> myDeals = new ArrayList<>();
        for(Product product : lastDeals){
            if(product.getMember().getId().equals(member.getId())){
                myDeals.add(product);
            }
        }
        return myDeals;
    }

    /*
    메인에 보여야 하는 모든 마감임박 상품들 (이미 정렬됨)
     */
    @Override
    public List<Product> getAllLastDeals(){
        return productRepository.getLastDealProducts();
    }

    /*
    메인에 보여야 하는 모든 오픈 예정 상품들 (이미 정렬됨)
     */
    @Override
    public List<Product> getAllUpcomingDeals(){
        return productRepository.getUpcomingProducts();
    }

    /*
    메인에 보여야 하는 모든 현재 거래중인 상품들 (이미 정렬됨)
     */
    @Override
    public List<Product> getAllOngoingDeals(){
        return productRepository.getOngoingProducts();
    }

    /*
    새 상품 저장
     */
    @Override
    public Product saveProduct(Product product){
        Product newProduct = productRepository.save(product);

        return newProduct != null ? newProduct : null;
    }

    /*
    상품 삭제
     */
    @Override
    public boolean deleteProduct(Product product){
        try{
            productRepository.delete(product);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /*
    상품 수정
     */
    @Override
    public Product updateProduct(Product product){
        Product newProduct = productRepository.save(product);
        return newProduct != null ? newProduct : null;
    }

    
    //나선주
    @Override
    public Optional<Product> getProduct(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Page<Product> getProductsByKeyword(int page, String searchType, String keyword) {
        PageRequest pageRequest = PageRequest.of(page, 20);

        if(searchType.equals("open")) {
            return productRepository.findAllByOpenTimeGreaterThanAndTitleContainingOrderByOpenTimeAsc(pageRequest,LocalDateTime.now(),keyword);
        } else {
            return productRepository.findAllByCloseTimeGreaterThanAndTitleContainingOrderByCloseTimeAsc(pageRequest,LocalDateTime.now(),keyword);
        }
    }

    @Override
    public List<Product> getOngoingAuctions() {
        var result = new ArrayList<Product>();

        for(var product : productRepository.getOngoingProducts()){
            if(product.getSellingType().equals("경매")){
                result.add(product);
            }
        }

        return result;
    }


}
