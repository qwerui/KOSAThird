package com.kosa.tm.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kosa.tm.domain.alarm.Alarm;
import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "member_seq")
    @SequenceGenerator(name = "member_seq" , sequenceName = "MEMBER_SEQ", allocationSize = 1)
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

//    @Column(name = "MEMBER_NAME", nullable = false, length = 255)
//    private String memberName;
//
//    @Column(name = "MEMBER_PASSWORD", nullable = true, length = 255)
//    private String memberPassword;

    @Column(nullable = true)
    private String name;

    @Column(name = "EMAIL", nullable = true, length = 255)
    private String email;

    @Column(name = "ROLE", nullable = true)
    private String role;  // 기업, 개인

    @Column(name = "WALLET", nullable = true)
    private Long wallet;  // 지갑 (외래키로 연결될 수 있음)

    @Column(name = "MEMBER_TEMP", nullable = true)
    private Double memberTemp;  // 유저 온도 (소수점 가능성을 고려하여 Double 사용)

    @Column(name = "INTRODUCE", length = 255, nullable = true)
    private String introduce;  // 사용자 소개

    @Column(name = "DISCONNECTION_TIME", nullable = true)
    private LocalDateTime disconnectionTime;  // 접속 종료 시간


    @Column(name = "PROVIDER", nullable = true)
    private String provider; // OAuth2 공급자 (구글, 페이스북 등)

    @Column(name = "PROVIDER_ID", nullable = true)
    private String providerId; // 공급자의 사용자 ID

    @Column(name = "PICTURE", nullable = true)
    private String picture; // 프로필 사진 URL


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Alarm> alarms = new ArrayList<>();


    // OAuth2 사용자를 업데이트하는 메서드
    public Member updateOAuth2User(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    // 생성자 및 기타 메서드들
    @Builder
    public Member(String name, String email, String picture, String memberRole,  String provider, String providerId) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = memberRole;
        this.provider = provider;
        this.providerId = providerId;
    }

    public Member() {
    }
}

