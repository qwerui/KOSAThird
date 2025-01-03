package com.kosa.tm.domain.product_buying;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/**
 * author : 최혜령
 * date : 2024-08-19
 * description : 상품 구매 상세 서비스 구현
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         최혜령           1차 완성
 */
@Service
public class ProductBuyingServiceImpl implements ProductBuyingService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductBuyingRepository productBuyingRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Override
    public List<ProductBuying> getProductBuyingByMember(Member member){
        return productBuyingRepository.findByMember(member);
    }

    @Override
    public List<ProductBuying> getProductBuyingByProductId(Product product){
        return productBuyingRepository.findByProduct(product);
    }

    @Override
    public List<ProductBuying> getProductBuyingByProductIdAndMember(Long productId, Member member){
        List<ProductBuying> returnList = new ArrayList<>();
        List<ProductBuying> getAllProductsforMember =productBuyingRepository.findByMember(member);
        for(ProductBuying productBuying : getAllProductsforMember){
            if(productBuying.getProduct().getId().equals(productId)){
                returnList.add(productBuying);
            }
        }
        return returnList;
    }

//    @Override
//    public ProductBuying buy(Member member, Product product, ProductBuying productBuying){
//        productBuying.setMember(member);
//        productBuying.setProduct(product);
//
//        Integer productQuantity = product.getQuantity();
//        if(productQuantity-productBuying.getQuantity()>=0){
//            product.setQuantity(productQuantity-productBuying.getQuantity());
//            productService.updateProduct(product);
//            ProductBuying newOrder = productBuyingRepository.save(productBuying);
//            newOrder.setState(1); // 결제만 한 상태 : 1 배송중 : 2 배송완료 : 3 구매확정 : 4
//            productBuyingRepository.save(newOrder);
//            member.setWallet((long) (member.getWallet() + (long)(product.getPrice())*0.1));
//            memberService.saveMember(member);
//            return newOrder;
//        }
//        return null;
//    }
    @Override
    public ProductBuying buy(Member member, Product product, ProductBuying productBuying) {
        productBuying.setMember(member);
        productBuying.setProduct(product);

        Integer productQuantity = product.getQuantity();
        if (productQuantity - productBuying.getQuantity() >= 0) {
            product.setQuantity(productQuantity - productBuying.getQuantity());
            productService.updateProduct(product);

            productBuying.setState(1); // 결제만 한 상태
            ProductBuying newOrder = productBuyingRepository.save(productBuying);

            // 회원 지갑 업데이트
            member.setWallet(member.getWallet() + (long) (product.getPrice() * 0.1));
            memberRepository.save(member);
            return newOrder;
        }
        return null;
    }


    @Override
    public boolean cancel(Member member, Product product, ProductBuying productBuying){
        if(productBuying.getMember().equals(member) && productBuying.getProduct().equals(product)){
            productBuyingRepository.delete(productBuying);
            return true;
        }
        return false;
    }

    /*
    호출때마다 한단계씩 올라갑니다.
     */
    @Override
    public ProductBuying updateStatus(Product product, ProductBuying productBuying){
        if(productBuying.getProduct().equals(product)){
            if(productBuying.getState() + 1 <= 4){
                productBuying.setState(productBuying.getState()+1);
                return productBuyingRepository.save(productBuying);
            }
        }
        return null;
    }

    //나선주
    @Override
    public ProductBuying completeBuying(ProductBuying productBuying) {
        //포인트, address등 반영 결제 update
        productBuying.setPurchasePrice(productBuying.getPurchasePrice());
        productBuying.setPurchaseTime(productBuying.getPurchaseTime());
        productBuying.setState(productBuying.getState());
        productBuying.setAddress(productBuying.getAddress());
        productBuying.setBuyingMemo(productBuying.getBuyingMemo());
        return productBuyingRepository.save(productBuying);
    }

    @Override
    public List<Object[]> getProductsAndBuyingStatesByMemberEmail(String email) {
        return productBuyingRepository.findProductsAndBuyingStatesByMemberEmail(email);
    }


}
