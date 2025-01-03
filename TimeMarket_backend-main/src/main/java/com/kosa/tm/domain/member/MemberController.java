package com.kosa.tm.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;

    @PostMapping
    public Member createMember(@RequestBody Member member) {
        return memberService.saveMember(member);
    }

    //나선주
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/payment/{memberId}")
    public Member getMember(@PathVariable("memberId") Long memberId) {
        return memberService.findMemberById(memberId).get();
    }
}
