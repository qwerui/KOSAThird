package com.kosa.tm.domain.oauth2;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberRole;
import com.kosa.tm.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // PasswordEncoder 추가

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Trying to load user by email: " + email);
        Member siteUser = this.memberService.findByEmail(email);
//        Member _siteUser = this.memberRepository.findByEmail(email);

//        if (_siteUser.isempty()) {
//            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
//        }
//        Member siteUser = _siteUser.get();
//        memberRepository.updateLoginTime(siteUser, LocalDateTime.now());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("개인".equals(siteUser.getRole())) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.INDIVIDUAL.getKey()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.BUSINESS.getKey()));
        }


        // Logging the authorities
        System.out.println("User authorities: " + authorities);

        return new User(siteUser.getEmail(), siteUser.getName(), authorities);
    }

    // 비밀번호 확인 메서드 추가
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
