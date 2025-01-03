package com.kosa.tm.domain.auction.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final AuctionWebSocketHandler auctionWebSocketHandler;

    public WebSocketConfig(AuctionWebSocketHandler auctionWebSocketHandler) {
        this.auctionWebSocketHandler = auctionWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(auctionWebSocketHandler, "/ws/auction/**")
                .setAllowedOrigins("*"); // 운영환경에서는 특정 도메인을 허용
    }
}
