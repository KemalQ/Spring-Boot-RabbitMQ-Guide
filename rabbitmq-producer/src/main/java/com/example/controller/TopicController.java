package com.example.controller;

import com.example.dto.SystemEventDTO;
import com.example.dto.UserEventDTO;
import com.example.service.RabbitMessagePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicController {
    private final RabbitMessagePublisher messageSender;


    public TopicController(RabbitMessagePublisher messageSender) {
        this.messageSender = messageSender;
    }

    @PostMapping("/topic/user/signup")
    public ResponseEntity<Void> sendUserSignup(@RequestBody UserEventDTO userEvent){
        messageSender.sendUserSignup(userEvent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/topic/user/login")
    public ResponseEntity<Void> sendUserLogin(@RequestBody UserEventDTO userEvent){
        messageSender.sendUserLogin(userEvent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/topic/error")
    public ResponseEntity<Void> sendSystemError(@RequestBody SystemEventDTO systemEvent){
        messageSender.sendSystemError(systemEvent);
        return ResponseEntity.ok().build();
    }
}
