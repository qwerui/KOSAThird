package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByMember(Member member);
}
