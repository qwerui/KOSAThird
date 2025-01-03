package com.kosa.tm.domain.want;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.product.Product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Want {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long want_id;

    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn
    private Product product;

}
