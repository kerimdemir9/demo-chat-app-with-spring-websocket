package com.message.server.socket;

import com.message.server.data.model.MessageModel;
import com.message.server.data.service.MessageService;
import com.message.server.data.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final MessageService messageService;

    public MessageController(SimpMessagingTemplate messagingTemplate, UserService userService, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.messageService = messageService;
    }

    // ✅ 1️⃣ Broadcast Message (Public)
    @MessageMapping("/broadcast")
    public void broadcastMessage(@Payload Message message) {
        messageService.saveMessage(MessageModel.builder()
                .to(null)
                .from(userService.getUser(message.getFrom()))
                .type("BROADCAST")
                .text(message.getText())
                .build());
        
        messagingTemplate.convertAndSend("/topic/public", message);
    }

    // ✅ 2️⃣ Private Message (1-on-1)
    @MessageMapping("/private")
    public void privateMessage(@Payload Message message) {
        messageService.saveMessage(MessageModel.builder()
                .to(userService.getUser(message.getTo()))
                .from(userService.getUser(message.getFrom()))
                .type("PRIVATE")
                .text(message.getText())
                .build());
        
        // ✅ Send private message
        messagingTemplate.convertAndSendToUser(message.getTo(), "/queue/private", message);
        log.info("Message sent to: {}", message.getTo());
    }
}
