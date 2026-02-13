package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import com.example.service.RabbitMessagePublisher;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectController {

    private RabbitMessagePublisher messageSender;

    public DirectController(RabbitMessagePublisher messageSender){
        this.messageSender = messageSender;
    }

    @PostMapping("/sendOrder")
    public ResponseEntity<?> send(@Valid @RequestBody OrderDTO order){
        if (order == null){
            return ResponseEntity.badRequest().build();
        }
        messageSender.sendOrder(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<?> send(@Valid @RequestBody NotificationDTO notification){
        if (notification == null){
            return ResponseEntity.badRequest().build();
        }
        messageSender.sendNotification(notification);
        return ResponseEntity.ok().build();
    }
}