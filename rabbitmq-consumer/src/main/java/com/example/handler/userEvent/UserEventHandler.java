package com.example.handler.userEvent;

import com.example.dto.UserEventDTO;
import com.example.enums.UserEventType;

public interface UserEventHandler {
    UserEventType getUserEventType();

    void handleUserEvent(UserEventDTO userEvent);
}
