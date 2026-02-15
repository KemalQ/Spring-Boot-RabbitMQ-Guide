package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.service.RabbitMessagePublisher;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fanout")
public class FanoutController {
    private RabbitMessagePublisher messageSender;

    public FanoutController(RabbitMessagePublisher messageSender){
        this.messageSender = messageSender;
    }

    @PostMapping("/notify")
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotificationDTO notification){
        messageSender.sendFanoutNotification(notification);
        return ResponseEntity.ok().build();
    }
}
