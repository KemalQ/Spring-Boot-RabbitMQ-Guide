package com.example.controller;

import com.example.dto.PriorityMessageDTO;
import com.example.service.RabbitMessagePublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/headers")
public class HeadersController {
    private RabbitMessagePublisher messageSender;

    public HeadersController(RabbitMessagePublisher messageSender){
        this.messageSender = messageSender;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendPriorityMessage(@RequestBody PriorityMessageDTO priorityMessage,
                                                    @RequestParam String priority){
        if (!Arrays.asList("high", "low").contains(priority)) {
            throw new IllegalArgumentException("Invalid priority. Must be 'high' or 'low'");
        }
        messageSender.sendPriorityMessage(priorityMessage, priority);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/send")
//    public ResponseEntity<Void> sendMediumPriorityMessage(@RequestBody PriorityMessageDTO priorityMessage,
//                                                    @RequestParam Boolean urgent){
//        if (!Arrays.asList("high", "low").contains(urgent)) {
//            throw new IllegalArgumentException("Invalid priority. Must be 'high' or 'low'");
//        }
//        messageSender.sendMediumPriorityMessage(priorityMessage, urgent);
//        return ResponseEntity.ok().build();
//    }
}
