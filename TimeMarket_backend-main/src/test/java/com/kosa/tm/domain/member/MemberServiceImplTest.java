package com.kosa.tm.domain.member;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/*

@SpringBootTest
@Transactional
@Rollback
public class MemberServiceImplTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setMemberName("John Doe");
        member.setMemberPassword("password123");
        member.setEmail("john.doe@example.com");

        member.setIntroduce("Hello, I am John Doe.");
    }

    @Test
    void testCreateMember() {
        // Create
        Member savedMember = memberRepository.save(member);
        assertThat(savedMember.getId()).isNotNull();
        assertEquals(savedMember.getMemberName(), "John Doe");
    }

    @Test
    void testFindAllMembers() {
        // Create some members
        memberRepository.save(member);
        Member anotherMember = new Member();
        anotherMember.setMemberName("Jane Doe");
        anotherMember.setMemberPassword("password123");
        anotherMember.setEmail("jane.doe@example.com");

        anotherMember.setIntroduce("Hello, I am Jane Doe.");
        memberRepository.save(anotherMember);

        // Read
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    void testFindMemberById() {
        // Create
        Member savedMember = memberRepository.save(member);


        // Read by ID
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());
        assertTrue(foundMember.isPresent());
        assertEquals(foundMember.get().getMemberName(), "John Doe");
    }


    @Test
    void testUpdateMember() {
        // Create
        Member savedMember = memberRepository.save(member);

        // Update
        savedMember.setMemberName("John Updated");
        memberRepository.save(savedMember);

        // Read
        Optional<Member> updatedMember = memberRepository.findById(savedMember.getId());
        assertTrue(updatedMember.isPresent());
        assertEquals(updatedMember.get().getMemberName(), "John Updated");
    }

    @Test
    void testDeleteMember() {
        // Create
        Member savedMember = memberRepository.save(member);

        // Delete
        memberRepository.deleteById(savedMember.getId());

        // Verify Deletion
        Optional<Member> deletedMember = memberRepository.findById(savedMember.getId());
        assertTrue(deletedMember.isEmpty());
    }
}*/
