package com.kosa.tm.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



/**
 * author : 오재혁
 * date : 2024-08-20
 * description : 멤버 서비스 구현클래스
 * 요약 :
 * <p>
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-19         최혜령          최초 생성
 * 2024-08-20         오재혁           1차 완성
 */


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    @Autowired
    private final MemberRepository memberRepository;


    public List<Member> findAllMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional
    public Member saveMember(Member member) {
        member.setMemberTemp((double)80);
        member.setWallet((long)0);
        member.setRole("개인");
        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public void updateUserName(String email, String newName) {

        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            member.setName(newName);
            memberRepository.save(member);
        }

    }


    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public void updateIntroduce(String email, String newIntroduce) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            member.setIntroduce(newIntroduce);
            memberRepository.save(member);
        }
    }
}
