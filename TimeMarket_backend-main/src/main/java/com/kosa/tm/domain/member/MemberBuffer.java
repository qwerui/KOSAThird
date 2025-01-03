package com.kosa.tm.domain.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberBuffer {
    private Long id;
    private String name;
    private String email;
    private String role;  // 기업, 개인
    private Long wallet;  // 지갑 (외래키로 연결될 수 있음)
    private Double memberTemp;  // 유저 온도 (소수점 가능성을 고려하여 Double 사용)
    private String introduce;  // 사용자 소개
    private String picture; // 프로필 사진 URL

    public void setMember(Member member){
        id = member.getId();
        name = member.getName();
        email = member.getEmail();
        role = member.getRole();
        wallet = member.getWallet();
        memberTemp = member.getMemberTemp();
        introduce = member.getIntroduce();
        picture = member.getPicture();
    }
}
