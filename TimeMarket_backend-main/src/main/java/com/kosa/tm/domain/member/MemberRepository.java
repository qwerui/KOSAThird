package com.kosa.tm.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    Optional<Member> findByEmail(String email);
    Member findByEmail(String email);

    @Modifying
    @Query("UPDATE Member m SET m.introduce = :introduce WHERE m.email = :email")
    void updateIntroduce(@Param("email") String email, @Param("introduce") String introduce);

    @Modifying
    @Query("UPDATE Member m SET m.name = :name WHERE m.email = :email")
    void updateUserName(@Param("email") String email, @Param("name") String name);

}
