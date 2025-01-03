package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import com.kosa.tm.domain.want.WantService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * author : 홍제기
 * date : 2024-08-23
 * description : 오픈 예정 상품 알림 발송 클래스
 * 요약 : 멀티스레드를 사용해 1초마다 오픈 여부를 체크해 알림을 발송하는 클래스
 * ===========================================================
 * DATE            AUTHOR             NOTE
 * —————————————————————————————
 * 2024-08-23         홍제기          최초 생성
 */

@Component
@RequiredArgsConstructor
public class WantAlarmScheduler {

    private final AlarmService alarmService;
    private final ProductService productService;
    private final AuctionAlarmScheduler auctionAlarmScheduler;

    private int roundRobinIndex = -1;
    private final List<UpcomingThread> runnerPool = new ArrayList<>();

    @Value("${alarm.thread.count}")
    private int threadCount;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        var upcoming = productService.getAllUpcomingDeals();

        final int size = (upcoming.size() / threadCount) + 1;

        for(int i=0;i<threadCount;i++){
            UpcomingThread runner = new UpcomingThread();
            runnerPool.add(runner);

            runner.upcomingQueue.addAll(upcoming.subList(Math.min(i*size, upcoming.size()), Math.min(i*size+size, upcoming.size())));

            Thread thread = new Thread(runner);
            thread.start();
        }
    }

    public void addUpcomingProduct(Product product){
        roundRobinIndex = (roundRobinIndex + 1) % threadCount;
        runnerPool.get(roundRobinIndex).upcomingQueue.add(product);
    }

    // 오픈 시간 체킹 스레드
    class UpcomingThread implements Runnable {

        public final PriorityBlockingQueue<Product> upcomingQueue = new PriorityBlockingQueue<>(1024, Comparator.comparing(Product::getOpenTime));

        @Override
        public void run() {
            while(true){
                Product nextOpen = upcomingQueue.peek();

                if(nextOpen == null) {
                    try{
                        Thread.sleep(60000);
                    } catch (Exception e) {
                        break;
                    }
                    continue;
                }

                if(LocalDateTime.now().isAfter(nextOpen.getOpenTime())){
                    var wants = alarmService.getAllAlarmsByProduct(nextOpen);

                    for(var w : wants){
                        alarmService.notifyAlarm(w.getMember().getEmail()); //알림 발송
                    }

                    if(nextOpen.getSellingType().equals("경매")){
                        auctionAlarmScheduler.addUpcomingProduct(nextOpen);
                    }

                    upcomingQueue.poll();
                } else {
                    try{
                        Thread.sleep(60000);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
        }
    }
}
