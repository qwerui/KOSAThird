package com.kosa.tm.domain.want;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class WantService {
    private final WantRepository wantRepository;
    private final ProductRepository productRepository;

    //전체 조회
    public List<Want> getAllWants(){
        return wantRepository.findAll();
    }

    //wish id로 조회
    public Optional<Want> getWantById(Long id){
        return wantRepository.findById(id);
    }

    //생성
    @Transactional
    public Want createWant(Want want){
        return wantRepository.save(want);
    }

    //수정
    @Transactional
    public Want updateWant(Long id, Want want){
        if(wantRepository.existsById(id)){
            return wantRepository.save(want);
        }
        log.info("Want with id {} not found", id);
        return null;
    }

    //삭제
    @Transactional
    public void deleteWant(Long id){
        if(wantRepository.existsById(id)){
            wantRepository.deleteById(id);
        }
    }

    //삭제 재혁이 도전
    public boolean deleteWantsByProductID(Long productID){
        Optional<Product> productOptional = productRepository.findById(productID);

        if (productOptional.isPresent()) {

            Product product = productOptional.get();
            List<Want> wants = wantRepository.findAllByProduct(product);
            if( wants!= null && !wants.isEmpty()){
                wantRepository.deleteAll(wants);
                return true;
            }
        }
        return false;
    }

    //
    //한 유저의 찜목록 조회
    public List<Want> getWantsByMember(Member member){
        return wantRepository.findByMember(member);
    }

    //한 유저 찜목록 전체 삭제
    @Transactional
    public void deleteWantsByMember(Member member){
        if(wantRepository.findByMember(member).size()!=0){
            wantRepository.deleteByMember(member);
        }
    }

    public Want getWantByProduct(Product product){
        return wantRepository.findByProduct(product);
    }

    @Transactional
    public void deleteWantByProduct(Product product){
        if(wantRepository.findByProduct(product)!=null){
            wantRepository.deleteByProduct(product);
        }
    }

    public List<Want> getWantMemberByProduct(Product product) {return wantRepository.findAllByProduct(product);}
}
