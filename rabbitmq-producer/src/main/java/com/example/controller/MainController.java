package com.example.controller;

import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import com.example.service.MessageSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private MessageSender messageSender;

    public MainController(MessageSender messageSender){
        this.messageSender = messageSender;
    }

    @PostMapping("/sendOrder")
    public ResponseEntity<?> send(@RequestBody OrderDTO order){
        if (order == null){
            return ResponseEntity.badRequest().build();
        }
        messageSender.sendOrder(order);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<?> send(@RequestBody NotificationDTO notification){
        if (notification == null){
            return ResponseEntity.badRequest().build();
        }
        messageSender.sendNotification(notification);
        return ResponseEntity.ok().build();
    }
}