package com.example.application;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import com.example.enums.ChannelType;
import com.example.enums.UserEventType;
import com.example.handler.notification.NotificationHandler;
import com.example.handler.notification.NotificationHandlerRegistry;
import com.example.handler.userEvent.UserEventHandler;
import com.example.handler.userEvent.UserEventHandlerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessagingApplicationServiceImpl Unit Test")
public class MessagingApplicationServiceImplTest {
    private MessagingApplicationServiceImpl service;

    @Mock
    private MessageValidator messageValidator;

    @Mock
    private NotificationHandlerRegistry notificationRegistry;

    @Mock
    private UserEventHandlerRegistry userEventRegistry;

    @Mock
    private NotificationHandler smsHandler;

    @Mock
    private NotificationHandler emailHandler;

    @Mock
    private NotificationHandler pushHandler;

    @Mock
    private NotificationHandler notificationHandler;

    @Mock
    private UserEventHandler loginHandler;

    @Mock
    private UserEventHandler signupHandler;

    @BeforeEach
    void setUp(){
        service = new MessagingApplicationServiceImpl(
                messageValidator, notificationRegistry, userEventRegistry);
    }

    @Test
    @DisplayName("Should process order successfully")
    public void handleOrder_withValidOrder_shouldValidateAndProcess(){

        OrderDTO order = new OrderDTO(1L, "Laptop", 2);
        doNothing().when(messageValidator).validateOrder(order);

        assertDoesNotThrow(()->service.handleOrder(order));

        verify(messageValidator, times(1)).validateOrder(order);
    }

    //  ORDER TEST      DIRECT EXCHANGE

    @Test
    @DisplayName("handleOrder() should throw exception when validation fails")
    void handleOrder_withInvalidOrder_shouldThrowException() {
        // Given
        OrderDTO order = new OrderDTO(null, "", 0);
        doThrow(new IllegalArgumentException("Invalid Order"))
                .when(messageValidator).validateOrder(order);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.handleOrder(order));

        verify(messageValidator, times(1)).validateOrder(order);
    }

    //  NOTIFICATION TESTS  - STRATEGY PATTERN (DIRECT + FANOUT)
    @Test
    @DisplayName("handleNotification() should validate and delegate to EMAIL handler")
    void handleNotification_withEmailChannel_shouldDelegateToEmailHandler() {
        // Given
        NotificationDTO notification = new NotificationDTO(100L, "Email message");
        doNothing().when(messageValidator).validateNotification(notification);
        when(notificationRegistry.get(ChannelType.EMAIL)).thenReturn(emailHandler);

        // When
        assertDoesNotThrow(() -> service.handleNotification(notification, ChannelType.EMAIL));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
        verify(notificationRegistry, times(1)).get(ChannelType.EMAIL);
        verify(emailHandler, times(1)).handleNotification(notification);
    }

    @Test
    @DisplayName("handleNotification() should validate and delegate to SMS handler")
    void handleNotification_withSmsChannel_shouldDelegateToSmsHandler() {
        // Given
        NotificationDTO notification = new NotificationDTO(200L, "SMS message");
        doNothing().when(messageValidator).validateNotification(notification);
        when(notificationRegistry.get(ChannelType.SMS)).thenReturn(smsHandler);

        // When
        service.handleNotification(notification, ChannelType.SMS);

        // Then
        verify(messageValidator).validateNotification(notification);
        verify(notificationRegistry).get(ChannelType.SMS);
        verify(smsHandler).handleNotification(notification);
    }

    @Test
    @DisplayName("handleNotification() should validate and delegate to PUSH handler")
    void handleNotification_withPushChannel_shouldDelegateToPushHandler() {
        // Given
        NotificationDTO notification = new NotificationDTO(300L, "Push message");
        doNothing().when(messageValidator).validateNotification(notification);
        when(notificationRegistry.get(ChannelType.PUSH)).thenReturn(pushHandler);

        // When
        service.handleNotification(notification, ChannelType.PUSH);

        // Then
        verify(messageValidator).validateNotification(notification);
        verify(notificationRegistry).get(ChannelType.PUSH);
        verify(pushHandler).handleNotification(notification);
    }

    @Test
    @DisplayName("handleNotification() should validate and delegate to NOTIFICATION handler")
    void handleNotification_withNotificationChannel_shouldDelegateToNotificationHandler() {
        // Given
        NotificationDTO notification = new NotificationDTO(400L, "Notification message");
        doNothing().when(messageValidator).validateNotification(notification);
        when(notificationRegistry.get(ChannelType.NOTIFICATION)).thenReturn(notificationHandler);

        // When
        service.handleNotification(notification, ChannelType.NOTIFICATION);

        // Then
        verify(messageValidator).validateNotification(notification);
        verify(notificationRegistry).get(ChannelType.NOTIFICATION);
        verify(notificationHandler).handleNotification(notification);
    }

    @Test
    @DisplayName("handleNotification() should throw exception when validation fails")
    void handleNotification_withInvalidNotification_shouldThrowException() {
        // Given
        NotificationDTO notification = new NotificationDTO(null, "");
        doThrow(new IllegalArgumentException("Invalid Notification"))
                .when(messageValidator).validateNotification(notification);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.handleNotification(notification, ChannelType.EMAIL));

        verify(messageValidator).validateNotification(notification);
        verify(notificationRegistry, never()).get(any());
    }

    @Test
    @DisplayName("handleNotification() should throw exception when handler not found")
    void handleNotification_withUnsupportedChannel_shouldThrowException() {
        // Given
        NotificationDTO notification = new NotificationDTO(500L, "Message");
        doNothing().when(messageValidator).validateNotification(notification);
        when(notificationRegistry.get(ChannelType.EMAIL)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.handleNotification(notification, ChannelType.EMAIL));

        assertEquals("Unsupported channel type: EMAIL", exception.getMessage());
        verify(messageValidator).validateNotification(notification);
        verify(notificationRegistry).get(ChannelType.EMAIL);
    }

    //  USER EVENT TESTS - STRATEGY PATTERN

    @Test
    @DisplayName("handleUserEvent() should validate and delegate to SIGNUP handler")
    void handleUserEvent_withSignupEvent_shouldDelegateToSignupHandler() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "SIGNUP", 1L, "john_doe", "john@example.com",
                Instant.now(), "192.168.1.1"
        );
        doNothing().when(messageValidator).validateUserEvent(userEvent);
        when(userEventRegistry.get(UserEventType.SIGNUP)).thenReturn(signupHandler);

        // When
        service.handleUserEvent(userEvent, UserEventType.SIGNUP);

        // Then
        verify(messageValidator).validateUserEvent(userEvent);
        verify(userEventRegistry).get(UserEventType.SIGNUP);
        verify(signupHandler).handleUserEvent(userEvent);
    }

    @Test
    @DisplayName("handleUserEvent() should validate and delegate to LOGIN handler")
    void handleUserEvent_withLoginEvent_shouldDelegateToLoginHandler() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "LOGIN", 2L, "jane_doe", "jane@example.com",
                Instant.now(), "192.168.1.2"
        );
        doNothing().when(messageValidator).validateUserEvent(userEvent);
        when(userEventRegistry.get(UserEventType.LOGIN)).thenReturn(loginHandler);

        // When
        service.handleUserEvent(userEvent, UserEventType.LOGIN);

        // Then
        verify(messageValidator).validateUserEvent(userEvent);
        verify(userEventRegistry).get(UserEventType.LOGIN);
        verify(loginHandler).handleUserEvent(userEvent);
    }

    @Test
    @DisplayName("handleUserEvent() should throw exception when validation fails")
    void handleUserEvent_withInvalidEvent_shouldThrowException() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(null, null, null, null, null, null);
        doThrow(new IllegalArgumentException("Invalid UserEvent"))
                .when(messageValidator).validateUserEvent(userEvent);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.handleUserEvent(userEvent, UserEventType.SIGNUP));

        verify(messageValidator).validateUserEvent(userEvent);
        verify(userEventRegistry, never()).get(any());
    }

    @Test
    @DisplayName("handleUserEvent() should throw exception when handler not found")
    void handleUserEvent_withUnsupportedEventType_shouldThrowException() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "TEST", 1L, "test", "test@test.com",
                Instant.now(), "127.0.0.1"
        );
        doNothing().when(messageValidator).validateUserEvent(userEvent);
        when(userEventRegistry.get(UserEventType.SIGNUP)).thenReturn(null);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.handleUserEvent(userEvent, UserEventType.SIGNUP));

        assertEquals("Unsupported channel type: SIGNUP", exception.getMessage());
        verify(messageValidator).validateUserEvent(userEvent);
        verify(userEventRegistry).get(UserEventType.SIGNUP);
    }

    //  SYSTEM ERROR TESTS (TOPIC EXCHANGE)

    @Test
    @DisplayName("handleSystemErrorMessage() should validate and process system event")
    void handleSystemErrorMessage_withValidEvent_shouldValidateAndProcess() {
        // Given
        SystemEventDTO systemEvent = new SystemEventDTO(
                "AUTH_SERVICE", "ERROR", "DB_001",
                "Database connection failed",
                Instant.now(),
                Map.of("host", "db-server", "port", "5432")
        );
        doNothing().when(messageValidator).validateSystemErrorEvent(systemEvent);

        // When
        assertDoesNotThrow(() -> service.handleSystemErrorMessage(systemEvent));

        // Then
        verify(messageValidator, times(1)).validateSystemErrorEvent(systemEvent);
    }

    @Test
    @DisplayName("handleSystemErrorMessage() should throw exception when validation fails")
    void handleSystemErrorMessage_withInvalidEvent_shouldThrowException() {
        // Given
        SystemEventDTO systemEvent = new SystemEventDTO(null, null, null, null, null, null);
        doThrow(new IllegalArgumentException("Invalid SystemEvent"))
                .when(messageValidator).validateSystemErrorEvent(systemEvent);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.handleSystemErrorMessage(systemEvent));

        verify(messageValidator).validateSystemErrorEvent(systemEvent);
    }

    //  PRIORITY MESSAGE TESTS (HEADERS EXCHANGE - IF-ELSE)

    @Test
    @DisplayName("handlePriorityMessage() should validate and process HIGH priority message")
    void handlePriorityMessage_withHighPriority_shouldValidateAndProcess() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "ORDER_SERVICE", "Urgent order",
                "BUSINESS", Instant.now()
        );
        doNothing().when(messageValidator).validatePriorityMessage(message);

        // When
        assertDoesNotThrow(() -> service.handlePriorityMessage(message, ChannelType.HIGH));

        // Then
        verify(messageValidator, times(1)).validatePriorityMessage(message);
    }

    @Test
    @DisplayName("handlePriorityMessage() should validate and process LOW priority message")
    void handlePriorityMessage_withLowPriority_shouldValidateAndProcess() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "NOTIFICATION_SERVICE", "Regular task",
                "SYSTEM", Instant.now()
        );
        doNothing().when(messageValidator).validatePriorityMessage(message);

        // When
        assertDoesNotThrow(() -> service.handlePriorityMessage(message, ChannelType.LOW));

        // Then
        verify(messageValidator, times(1)).validatePriorityMessage(message);
    }

    @Test
    @DisplayName("handlePriorityMessage() should throw exception when validation fails")
    void handlePriorityMessage_withInvalidMessage_shouldThrowException() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(null, null, null, null);
        doThrow(new IllegalArgumentException("Invalid PriorityMessage"))
                .when(messageValidator).validatePriorityMessage(message);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.handlePriorityMessage(message, ChannelType.HIGH));

        verify(messageValidator).validatePriorityMessage(message);
    }

    //  EDGE CASES & INTEGRATION TESTS

    @Test
    @DisplayName("All methods should call validator exactly once")
    void allMethods_shouldCallValidatorExactlyOnce() {
        // Given
        OrderDTO order = new OrderDTO(1L, "Product", 1);
        NotificationDTO notification = new NotificationDTO(1L, "Message");
        UserEventDTO userEvent = new UserEventDTO("TEST", 1L, "user", "user@test.com", Instant.now(), "127.0.0.1");
        SystemEventDTO systemEvent = new SystemEventDTO("TEST", "INFO", "T01", "Test", Instant.now(), Map.of());
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO("TEST", "Test", "SYSTEM", Instant.now());

        when(notificationRegistry.get(ChannelType.EMAIL)).thenReturn(emailHandler);
        when(userEventRegistry.get(UserEventType.SIGNUP)).thenReturn(signupHandler);

        // When
        service.handleOrder(order);
        service.handleNotification(notification, ChannelType.EMAIL);
        service.handleUserEvent(userEvent, UserEventType.SIGNUP);
        service.handleSystemErrorMessage(systemEvent);
        service.handlePriorityMessage(priorityMessage, ChannelType.HIGH);

        // Then
        verify(messageValidator, times(1)).validateOrder(order);
        verify(messageValidator, times(1)).validateNotification(notification);
        verify(messageValidator, times(1)).validateUserEvent(userEvent);
        verify(messageValidator, times(1)).validateSystemErrorEvent(systemEvent);
        verify(messageValidator, times(1)).validatePriorityMessage(priorityMessage);
    }

    @Test
    @DisplayName("Strategy handlers should be called only after successful validation")
    void strategyHandlers_shouldBeCalledOnlyAfterValidation() {
        // Given
        NotificationDTO notification = new NotificationDTO(100L, "Test");
        doThrow(new IllegalArgumentException("Validation failed"))
                .when(messageValidator).validateNotification(notification);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> service.handleNotification(notification, ChannelType.EMAIL));

        // Verify handler was never called because validation failed
        verify(notificationRegistry, never()).get(any());
        verify(emailHandler, never()).handleNotification(any());
    }
}
