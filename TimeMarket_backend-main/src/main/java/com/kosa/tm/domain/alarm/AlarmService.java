package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.product.Product;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

public interface AlarmService {

    public List<Alarm> getAllAlarms();
    public Optional getAlarmById(Long id);


    public List getAlarmBymemberName(String memberName);
    public List<Alarm> getAlarmsByEmail(String email);

    public Alarm saveAlarm(Alarm alarm);

    public Alarm createAlarm(Alarm alarm);

    public void markRead(Long id);

    List<Alarm> getAlarmsByEmail2(String email);

    List<Alarm> getAllAlarmsByProduct(Product product);

    public void registerAlarmEvent(String email, SseEmitter emitter);
    public void notifyAlarm(String email);
}
