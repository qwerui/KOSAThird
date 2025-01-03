package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlarmSerivceImpl implements AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private AlarmRepositoryCustom alarmRepositoryCustom;

    // 알람 전송을 위한 접속자-Emitter 맵
    Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    @Transactional
    public Alarm saveAlarm(Alarm alarm) {
        return alarmRepository.save(alarm);
    }

    @Override
    public Alarm createAlarm(Alarm alarm) {
        // 알람 생성 시 알람 읽음은 기본 false
        alarm.setIsread(false);

        // 알람 시간이 현재 시간 보다 작으면 알람 즉시 발송
        if(alarm.getTime().isBefore(LocalDateTime.now())){
            notifyAlarm(alarm.getMember().getEmail());
        }

        return alarmRepository.save(alarm);
    }

    // 알람 읽었을 때 읽음 상태로 변경하는 메서드
    @Override
    @Transactional
    public void markRead(Long id) {
        var foundAlarm = alarmRepository.findById(id);
        if(foundAlarm.isEmpty()){
            return;
        }
        foundAlarm.get().setIsread(true);
    }

    @Override
    public List<Alarm> getAllAlarms() {
        return alarmRepository.findAll();
    }

    @Override
    public Optional getAlarmById(Long id) {
        return alarmRepository.findById(id);
    }


    @Override
    public List<Alarm> getAlarmBymemberName(String memberName) {

        return alarmRepositoryCustom.findAlarmsByMembername(memberName);
    }

    @Override
    public List<Alarm> getAlarmsByEmail(String email) {
        return alarmRepositoryCustom.findAlarmsByEmail(email);
    }

    @Override
    public List<Alarm> getAlarmsByEmail2(String email) {
        return alarmRepositoryCustom.findAlarmsByEmail2(email);
    }

    @Override
    public List<Alarm> getAllAlarmsByProduct(Product product) {
        return alarmRepositoryCustom.findAllAlarmsByProduct(product);
    }

    @Override
    public void registerAlarmEvent(String email, SseEmitter emitter) {

        emitters.put(email, emitter);

        emitter.onCompletion(() -> {
            emitters.remove(email);
        });

        emitter.onTimeout(emitter::complete);

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void notifyAlarm(String email) {
        var emitter = emitters.get(email);

        if(emitter == null){
            return;
        }
        try{
            emitter.send(SseEmitter.event().name("alarm").data("alarm notified"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
