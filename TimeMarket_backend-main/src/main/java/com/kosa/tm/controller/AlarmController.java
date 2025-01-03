package com.kosa.tm.controller;

import com.kosa.tm.domain.alarm.Alarm;
import com.kosa.tm.domain.alarm.AlarmBuffer;
import com.kosa.tm.domain.alarm.AlarmService;
import com.kosa.tm.domain.member.MemberRepository;
import com.kosa.tm.domain.member.MemberService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AlarmController {

    @Autowired
    AlarmService alarmService;

    @Autowired
    ProductService productService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @GetMapping("/api/register-alarm")
    public ResponseEntity<SseEmitter> connect(@RequestParam("user") String email){
        SseEmitter emitter = new SseEmitter();
        alarmService.registerAlarmEvent(email, emitter);
        return ResponseEntity.ok(emitter);
    }

    @GetMapping("/api/alarm")
    public List<AlarmBuffer> getAlarms(@RequestParam("user") String email){

        var result = new ArrayList<AlarmBuffer>();

        for(var alarm : alarmService.getAlarmsByEmail(email)){
            var buffer = new AlarmBuffer();

            buffer.setId(alarm.getId());
            buffer.setTitle(alarm.getTitle());
            buffer.setDescription(alarm.getDescription());
            buffer.setType(alarm.getType());
            buffer.setProduct(alarm.getProduct());
            buffer.setTime(alarm.getTime());

            result.add(buffer);
        }

        return result;
    }

    @GetMapping("/api/alarm/read")
    public ResponseEntity<Object> readAlarm(@RequestParam("alarm") Long id){

        alarmService.markRead(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/check-alarm")
    public Integer checkAlarmExists(@RequestParam("user") String email){
        return alarmService.getAlarmsByEmail(email).size();
    }

    @PostMapping("/api/reg-open-alarm/{productId}/{userEmail}")
    public ResponseEntity<String> register_productAlarm(@PathVariable Long productId, @PathVariable String userEmail){
        Alarm alarm = new Alarm();
        Product product = productService.getProduct(productId).get();
        alarm.setProduct(product);
        alarm.setIsread(false);
        alarm.setTime(product.getOpenTime());
        alarm.setMember(memberService.findByEmail(userEmail));
        alarm.setType("오픈");
        alarm.setTitle(product.getTitle() + " 제품이 오픈되었습니다!");
        boolean canInsert = true;
        List<Alarm> alarms = alarmService.getAlarmsByEmail2(userEmail);
        System.out.println("Number of Alarms: " + alarms.size());
        int i = 0;
        for(Alarm a : alarmService.getAlarmsByEmail(userEmail)){

            if(a.getProduct().getId() == product.getId()){
                i += 1;
                canInsert = false;
            }
        }
        System.out.println("count: " + i);
        System.out.println("canInsert: " + canInsert);
        if(canInsert){
            alarmService.saveAlarm(alarm);
            return ResponseEntity.ok("알람이 등록되었습니다.");
        }
        return ResponseEntity.badRequest().build();
    }
}
