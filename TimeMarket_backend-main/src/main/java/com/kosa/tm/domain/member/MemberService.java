package com.kosa.tm.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    public List<Member> findAllMembers();

    public Optional<Member> findMemberById(Long id) ;

    public Member saveMember(Member member);

    public void deleteMember(Long id) ;

//    boolean updateUserName(String userEmail, String newName);

    Member findByEmail(String email);

    void updateIntroduce(String email, String newIntroduce);

    void updateUserName(String userEmail,String newName);

}
