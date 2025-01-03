package com.kosa.tm.domain.auction.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Component
public class AuctionWebSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트에서 받은 메시지 처리
        String payload = message.getPayload();
        System.out.println("Received: " + payload);

        // 다른 클라이언트에게 메시지 전송
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen() && !s.equals(session)) {
                    s.sendMessage(new TextMessage(payload));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    // 메시지를 모든 클라이언트에게 전송하는 메서드
    public void broadcast(String message) {
        synchronized (sessions) {
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
