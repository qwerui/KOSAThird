package com.kosa.tm.domain.want;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WantRepository extends JpaRepository<Want, Long> {
    public List<Want> findByMember(Member member);
    public void deleteByMember(Member member);
    public Want findByProduct(Product product);
    public void deleteByProduct(Product product);
    public List<Want> findAllByProduct(Product product);
}
