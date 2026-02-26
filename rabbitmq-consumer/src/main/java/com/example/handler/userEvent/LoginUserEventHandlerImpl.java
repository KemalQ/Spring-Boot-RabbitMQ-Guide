package com.example.handler.userEvent;

import com.example.dto.UserEventDTO;
import com.example.enums.UserEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginUserEventHandlerImpl implements UserEventHandler {
    @Override
    public UserEventType getUserEventType() {
        return UserEventType.LOGIN;
    }

    @Override
    public void handleUserEvent(UserEventDTO userEvent) {
        log.info("✅ user.login.queue message: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());
        // ***
    }
}
