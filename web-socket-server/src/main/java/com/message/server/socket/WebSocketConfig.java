package com.message.server.socket;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // ✅ WebSocket connection endpoint
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Fallback for older browsers
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic", "/user"); // ✅ Enable in-memory message broker
        registry.setApplicationDestinationPrefixes("/app"); // ✅ Prefix for app messages
        registry.setUserDestinationPrefix("/user"); // ✅ Prefix for user messages
    }
}
