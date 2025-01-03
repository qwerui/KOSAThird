package com.kosa.tm.domain.oauth2;


import com.kosa.tm.domain.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attributes;

    public NaverResponse(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
}