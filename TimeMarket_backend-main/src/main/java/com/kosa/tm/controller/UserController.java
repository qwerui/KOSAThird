package com.kosa.tm.controller;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")  // 클라이언트의 도메인
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User))   {
            return ResponseEntity.status(401).body("Unauthorized: Invalid or missing authentication.");
        }

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String registrationId = oAuth2User.getAttributes().get("issuer").toString(); // OAuth2 제공자 확인
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();   // OAuth2 제공자 확인
        System.out.println("reigstraionID ::::::::::::::::::::::::::" + registrationId);

        Map<String, Object> userInfo = new HashMap<>();

        if (registrationId.contains("kakao")) {
            // 카카오 계정 정보 처리
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            String email = (String) kakaoAccount.get("email");

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            String nickname = (String) profile.get("nickname");

            Long wallet = memberService.findByEmail(email).getWallet();
            String introduce = memberService.findByEmail(email).getIntroduce();
            Double temp = memberService.findByEmail(email).getMemberTemp();
            String role = memberService.findByEmail(email).getRole();
            String currentname = memberService.findByEmail(email).getName();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

            userInfo.put("wallet", wallet);
            userInfo.put("introduce", introduce);
            userInfo.put("temp", temp);
            userInfo.put("role", role);
            userInfo.put("currentname", currentname);



            System.out.println("kakao accountttttttttttttttttttttttttttttt :::  " + nickname + email + wallet + introduce + temp + role);
        } else if (registrationId.contains("google")) {
            // 구글 계정 정보 처리
            String email = (String) oAuth2User.getAttributes().get("email");
            String nickname = (String) oAuth2User.getAttributes().get("name");

            Long wallet = memberService.findByEmail(email).getWallet();
            String introduce = memberService.findByEmail(email).getIntroduce();
            Double temp = memberService.findByEmail(email).getMemberTemp();
            String role = memberService.findByEmail(email).getRole();
            String currentname = memberService.findByEmail(email).getName();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

            userInfo.put("wallet", wallet);
            userInfo.put("introduce", introduce);
            userInfo.put("temp", temp);
            userInfo.put("role", role);
            userInfo.put("currentname", currentname);

            System.out.println("googleeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee:::  " +nickname + email + wallet + introduce + temp + role);
        } else if(registrationId.contains("naver")){
            // 네이버 계정 정보 처리
            var res = (Map<String, Object>)oAuth2User.getAttributes().get("response");
            String email = (String)res.get("email");
            String nickname = (String)res.get("name");

            Long wallet = memberService.findByEmail(email).getWallet();
            String introduce = memberService.findByEmail(email).getIntroduce();
            Double temp = memberService.findByEmail(email).getMemberTemp();
            String role = memberService.findByEmail(email).getRole();
            String currentname = memberService.findByEmail(email).getName();

            userInfo.put("nickname", nickname);
            userInfo.put("email", email);

            userInfo.put("wallet", wallet);
            userInfo.put("introduce", introduce);
            userInfo.put("temp", temp);
            userInfo.put("role", role);
            userInfo.put("currentname", currentname);
        }
        else {
            return ResponseEntity.status(400).body("Unsupported provider: " + registrationId);
        }

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/update-name")
    public ResponseEntity<String> updateUserName(@RequestBody Map<String, String> request) {
        System.out.println("updateeeeeeeeeeeeeeeeeeeeeeeeeenameeeeeeeeeeeeeeeeeeeeeeeeee");
        String newName = request.get("name");
        String userEmail = request.get("email");

        Member memeberEntity = memberService.findByEmail(userEmail);
        System.out.println("멤버 이메일로 멤버 엔터티 찾기 ::::::: " + memeberEntity );

        if (memeberEntity != null) {
            // introduce 필드만 업데이트하도록 변경
            memberService.updateUserName(userEmail, newName);

            return ResponseEntity.ok("소개가 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("소개 업데이트에 실패했습니다.");
        }

//        if (memeberEntity != null) {
//            String currentName = memeberEntity.getName();
//
//            memeberEntity.setName(newName);
//
//            memberService.saveMember(memeberEntity);
//            System.out.println("멤버 서비스 저장합니다ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
//
//            return ResponseEntity.ok("이름이 성공적으로 업데이트되었습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이름 업데이트에 실패했습니다.");
//        }
    }


//    @PostMapping("/update-introduce")
//    public ResponseEntity<String> updateIntroduce(@RequestBody Map<String, String> request) {
//        String newIntroduce = request.get("introduce");
//        String userEmail = request.get("email");
//
//        Member memberEntity = memberService.findByEmail(userEmail);
//
//        if (memberEntity != null) {
//            memberEntity.setIntroduce(newIntroduce);  // introduce 필드를 업데이트
//            memberService.saveMember(memberEntity);
//
//            return ResponseEntity.ok("소개가 성공적으로 업데이트되었습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("소개 업데이트에 실패했습니다.");
//        }
//    }

    @PostMapping("/update-introduce")
    public ResponseEntity<String> updateIntroduce(@RequestBody Map<String, String> request) {
        String newIntroduce = request.get("introduce");
        String userEmail = request.get("email");

        Member memberEntity = memberService.findByEmail(userEmail);

        if (memberEntity != null) {
            // introduce 필드만 업데이트하도록 변경
            memberService.updateIntroduce(userEmail, newIntroduce);

            return ResponseEntity.ok("소개가 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("소개 업데이트에 실패했습니다.");
        }
    }

    @PostMapping("/recharge-wallet/{memberEmail}/{chargedPrice}")
    public ResponseEntity<String> rechargeWallet(@PathVariable String memberEmail, @PathVariable Double chargedPrice) {
        Member memberEntity = memberRepository.findByEmail(memberEmail);
        if (memberEntity != null) {
            memberEntity.setWallet((long) (memberEntity.getWallet() + chargedPrice));
            memberRepository.save(memberEntity);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

