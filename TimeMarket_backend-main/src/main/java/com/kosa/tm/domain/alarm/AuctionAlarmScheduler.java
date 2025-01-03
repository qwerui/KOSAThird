package com.kosa.tm.domain.alarm;

import com.kosa.tm.domain.auction.AuctionService;
import com.kosa.tm.domain.product.Product;
import com.kosa.tm.domain.product.ProductService;
import com.kosa.tm.domain.want.Want;
import com.kosa.tm.domain.want.WantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

@RequiredArgsConstructor
@Component
public class AuctionAlarmScheduler {
    private final AlarmService alarmService;
    private final ProductService productService;
    private final AuctionService auctionService;
    private final WantService wantService;

    private int roundRobinIndex = -1;
    private final List<AuctionThread> runnerPool = new ArrayList<>();

    @Value("${auction.thread.count}")
    private int threadCount;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        var upcoming = productService.getOngoingAuctions();

        final int size = (upcoming.size() / threadCount) + 1;

        for(int i=0;i<threadCount;i++){
            AuctionThread runner = new AuctionThread();
            runnerPool.add(runner);

            runner.upcomingQueue.addAll(upcoming.subList(Math.min(i*size, upcoming.size()), Math.min(i*size+size, upcoming.size())));

            Thread thread = new Thread(runner);
            thread.start();
        }
    }

    public synchronized void addUpcomingProduct(Product product){
        roundRobinIndex = (roundRobinIndex + 1) % threadCount;
        runnerPool.get(roundRobinIndex).upcomingQueue.add(product);
    }

    // 오픈 시간 체킹 스레드
    class AuctionThread implements Runnable {

        public final PriorityBlockingQueue<Product> upcomingQueue = new PriorityBlockingQueue<>(1024, Comparator.comparing(Product::getCloseTime));

        @Override
        public void run() {
            while(true){
                Product nextClose = upcomingQueue.peek();

                if(nextClose == null) {
                    try{
                        Thread.sleep(60000);
                    } catch (Exception e) {
                        break;
                    }
                    continue;
                }

                if(LocalDateTime.now().isAfter(nextClose.getCloseTime())){
                    var top = auctionService.findBuyerInfoByAuctionProduct(nextClose.getId());

                    Alarm alarm = new Alarm();
                    alarm.setTime(LocalDateTime.now());
                    alarm.setTitle(nextClose.getTitle()+" 상품이 낙찰되었습니다.");
                    alarm.setMember(top.getMember());
                    alarm.setProduct(nextClose);
                    alarm.setType("낙찰");
                    alarm.setDescription("낙찰");

                    alarmService.createAlarm(alarm);

                    Want want = new Want();
                    want.setProduct(nextClose);
                    want.setMember(top.getMember());

                    wantService.createWant(want);

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
