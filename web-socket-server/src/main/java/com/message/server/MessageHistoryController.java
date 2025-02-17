package com.message.server;

import com.message.server.data.model.MessageModel;
import com.message.server.data.service.MessageService;
import com.message.server.data.service.UserService;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageHistoryController {

    private final MessageService messageService;
    private final UserService userService;

    
    public MessageHistoryController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/messages_from")
    public ResponseEntity<List<MessageModel>> getMessagesFrom(@RequestParam String username) {
        val user = userService.getUser(username);
        
        return ResponseEntity.ok(messageService.getMessagesFrom(user));
    }

    @GetMapping("/messages_to")
    public ResponseEntity<List<MessageModel>> getMessagesTo(@RequestParam String username) {
        val user = userService.getUser(username);

        return ResponseEntity.ok(messageService.getMessagesTo(user));
    }
}

