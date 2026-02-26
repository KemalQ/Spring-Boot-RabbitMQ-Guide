package com.example.handler.userEvent;

import com.example.enums.UserEventType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserEventHandlerRegistry {
    private final Map<UserEventType, UserEventHandler> handler;

    public UserEventHandlerRegistry(List<UserEventHandler> handlerList) {
        this.handler = handlerList.stream()
                .collect(Collectors.toMap(UserEventHandler::getUserEventType, h->h));
    }

    public UserEventHandler get(UserEventType type){
        return handler.get(type);
    }
}
