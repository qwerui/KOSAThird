package com.kosa.tm.domain.member;

public enum MemberRole {
    INDIVIDUAL("ROLE_INDIVIDUAL"),  // 개인 사용자 역할
    BUSINESS("ROLE_BUSINESS");      // 기업 사용자 역할

    private final String key;

    MemberRole(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
