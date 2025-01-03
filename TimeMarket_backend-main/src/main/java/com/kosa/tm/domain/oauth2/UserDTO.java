package com.kosa.tm.domain.oauth2;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserDTO implements Serializable {
    private String name;
    private String email;
    private String picture;
    private String provider;
    private String providerId;

    public UserDTO(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
        this.picture = member.getPicture();
        this.provider = member.getProvider();
        this.providerId = member.getProviderId();
    }
}