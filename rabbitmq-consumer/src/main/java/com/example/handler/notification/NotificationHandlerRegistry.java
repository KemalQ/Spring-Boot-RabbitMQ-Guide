package com.example.handler.notification;

import com.example.enums.ChannelType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationHandlerRegistry {
    private final Map<ChannelType, NotificationHandler> handlers;


    public NotificationHandlerRegistry(List<NotificationHandler> handlerList) {
        this.handlers = handlerList.stream().
        collect(Collectors.toMap(NotificationHandler::getChannel, h -> h));
    }

    public NotificationHandler get(ChannelType type){
        return handlers.get(type);
    }
}
