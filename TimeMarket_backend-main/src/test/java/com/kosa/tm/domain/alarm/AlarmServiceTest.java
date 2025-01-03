package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.member.Member;
import com.kosa.tm.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class AlarmServiceTest {

    @Mock
    private AlarmRepository alarmRepository;

    @Mock
    private AlarmRepositoryCustom alarmRepositoryCustom;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AlarmSerivceImpl alarmService;

    private Member member;
    private Alarm alarm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new Member();
        member.setId(1L);
//        member.setMemberName("John Doe");

        alarm = new Alarm();
        alarm.setId(1L);
        alarm.setMember(member);
        alarm.setTitle("Test Alarm");

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
    }

    @Test
    void testGetAllAlarms() {
        List<Alarm> alarms = Arrays.asList(alarm);
        when(alarmRepository.findAll()).thenReturn(alarms);

        List<Alarm> result = alarmService.getAllAlarms();

        assertEquals(1, result.size());
        assertEquals("Test Alarm", result.get(0).getTitle());
        verify(alarmRepository, times(1)).findAll();
    }

    @Test
    void testGetAlarmById() {
        when(alarmRepository.findById(anyLong())).thenReturn(Optional.of(alarm));

        Optional<Alarm> result = alarmService.getAlarmById(1L);

        assertEquals(true, result.isPresent());
        assertEquals("Test Alarm", result.get().getTitle());
        verify(alarmRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAlarmByMemberName() {
        List<Alarm> alarms = Arrays.asList(alarm);
        when(alarmRepositoryCustom.findAlarmsByMembername(anyString())).thenReturn(alarms);

        List<Alarm> result = alarmService.getAlarmBymemberName("John Doe");

        assertEquals(1, result.size());
        assertEquals("Test Alarm", result.get(0).getTitle());
        verify(alarmRepositoryCustom, times(1)).findAlarmsByMembername(anyString());
    }

    @Test
    void testGetAlarmsById() {
        Alarm alarm1 = new Alarm();
        Alarm alarm2 = new Alarm();

        alarm1.setMember(member);
        alarm1.setId(1L);
        alarm1.setTitle("title");
        alarm1.setIsread(false);
        alarm1.setTime(LocalDateTime.now().minusHours(1));

//        // 특정 멤버 ID로 조회될 알람 리스트를 반환하도록 Mock 설정
//        when(alarmRepositoryCustom.findAlarmsByMemberId(1L)).thenReturn(Arrays.asList(alarm1));
//
//        // 서비스 메서드 호출
//        var result = alarmService.getAlarmsByMemberId(1L);
//
//        // 예상되는 결과 검증: 과거 시간의 알람만 반환되도록 검증
//        assertEquals(1, result.size());
//        assertTrue(result.get(0).getTime().isBefore(LocalDateTime.now()));
//
//        // 알람 저장 및 조회 메서드가 올바르게 호출되었는지 검증
//        verify(alarmRepositoryCustom, times(1)).findAlarmsByMemberId(1L);
    }
}
