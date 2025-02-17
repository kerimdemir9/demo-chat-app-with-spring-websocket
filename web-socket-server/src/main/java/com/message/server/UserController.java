package com.message.server;

import com.message.server.data.model.UserModel;
import com.message.server.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register_user")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }
}
