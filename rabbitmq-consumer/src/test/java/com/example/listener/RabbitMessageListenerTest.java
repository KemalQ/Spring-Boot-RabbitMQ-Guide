package com.example.listener;

import com.example.application.MessagingApplicationService;
import com.example.dto.*;
import com.example.enums.ChannelType;
import com.example.enums.UserEventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RabbitMessageListener Unit Tests")
public class RabbitMessageListenerTest {

    @Mock
    private MessagingApplicationService messagingService;

    private RabbitMessageListener listener;

    @BeforeEach
    void setUp() {
        listener = new RabbitMessageListener(messagingService);
    }

    // DIRECT EXCHANGE TESTS

    @Test
    @DisplayName("consumeOrder() should process valid order successfully")
    void consumeOrder_withValidOrder_shouldProcessSuccessfully() {
        // Given
        OrderDTO order = new OrderDTO(1L, "Laptop", 2);
        doNothing().when(messagingService).handleOrder(order);

        // When
        assertDoesNotThrow(() -> listener.consumeOrder(order));

        // Then
        verify(messagingService, times(1)).handleOrder(order);
    }

    @Test
    @DisplayName("consumeOrder() should throw AmqpRejectAndDontRequeueException on validation failure")
    void consumeOrder_withInvalidOrder_shouldReject() {
        // Given
        OrderDTO order = new OrderDTO(null, "", 0);
        doThrow(new IllegalArgumentException("Invalid Order"))
                .when(messagingService).handleOrder(order);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeOrder(order));

        verify(messagingService).handleOrder(order);
    }

    @Test
    @DisplayName("consumeNotification() should process valid notification successfully")
    void consumeNotification_withValidNotification_shouldProcessSuccessfully() {
        // Given
        NotificationDTO notification = new NotificationDTO(100L, "Message");
        doNothing().when(messagingService).handleNotification(notification, ChannelType.NOTIFICATION);

        // When
        assertDoesNotThrow(() -> listener.consumeNotification(notification));

        // Then
        verify(messagingService).handleNotification(notification, ChannelType.NOTIFICATION);
    }

    @Test
    @DisplayName("consumeNotification() should throw AmqpRejectAndDontRequeueException on validation failure")
    void consumeNotification_withInvalidNotification_shouldReject() {
        // Given
        NotificationDTO notification = new NotificationDTO(null, "");
        doThrow(new IllegalArgumentException("Invalid Notification"))
                .when(messagingService).handleNotification(notification, ChannelType.NOTIFICATION);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeNotification(notification));

        verify(messagingService).handleNotification(notification, ChannelType.NOTIFICATION);
    }

    // FANOUT EXCHANGE TESTS

    @Test
    @DisplayName("consumeEmailNotification() should process valid notification successfully")
    void consumeEmailNotification_withValidNotification_shouldProcessSuccessfully() {
        // Given
        NotificationDTO notification = new NotificationDTO(200L, "Email message");
        doNothing().when(messagingService).handleNotification(notification, ChannelType.EMAIL);

        // When
        assertDoesNotThrow(() -> listener.consumeEmailNotification(notification));

        // Then
        verify(messagingService).handleNotification(notification, ChannelType.EMAIL);
    }

    @Test
    @DisplayName("consumeEmailNotification() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeEmailNotification_withInvalidNotification_shouldReject() {
        // Given
        NotificationDTO notification = new NotificationDTO(null, null);
        doThrow(new IllegalArgumentException())
                .when(messagingService).handleNotification(notification, ChannelType.EMAIL);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeEmailNotification(notification));

        verify(messagingService).handleNotification(notification, ChannelType.EMAIL);
    }

    @Test
    @DisplayName("consumeSmsNotification() should process valid notification successfully")
    void consumeSmsNotification_withValidNotification_shouldProcessSuccessfully() {
        // Given
        NotificationDTO notification = new NotificationDTO(300L, "SMS message");

        // When
        assertDoesNotThrow(() -> listener.consumeSmsNotification(notification));

        // Then
        verify(messagingService).handleNotification(notification, ChannelType.SMS);
    }

    @Test
    @DisplayName("consumeSmsNotification() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeSmsNotification_withInvalidNotification_shouldReject() {
        // Given
        NotificationDTO notification = new NotificationDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handleNotification(notification, ChannelType.SMS);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeSmsNotification(notification));

        verify(messagingService).handleNotification(notification, ChannelType.SMS);
    }

    @Test
    @DisplayName("consumePushNotification() should process valid notification successfully")
    void consumePushNotification_withValidNotification_shouldProcessSuccessfully() {
        // Given
        NotificationDTO notification = new NotificationDTO(400L, "Push message");

        // When
        assertDoesNotThrow(() -> listener.consumePushNotification(notification));

        // Then
        verify(messagingService).handleNotification(notification, ChannelType.PUSH);
    }

    @Test
    @DisplayName("consumePushNotification() should throw AmqpRejectAndDontRequeueException on failure")
    void consumePushNotification_withInvalidNotification_shouldReject() {
        // Given
        NotificationDTO notification = new NotificationDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handleNotification(notification, ChannelType.PUSH);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumePushNotification(notification));

        verify(messagingService).handleNotification(notification, ChannelType.PUSH);
    }

    // TOPIC EXCHANGE TESTS

    @Test
    @DisplayName("consumeUserSignUpQueue() should process valid user event successfully")
    void consumeUserSignUpQueue_withValidUserEvent_shouldProcessSuccessfully() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "SIGNUP", 1L, "john_doe", "john@example.com",
                Instant.now(), "192.168.1.1"
        );

        // When
        assertDoesNotThrow(() -> listener.consumeUserSignUpQueue(userEvent));

        // Then
        verify(messagingService).handleUserEvent(userEvent, UserEventType.SIGNUP);
    }

    @Test
    @DisplayName("consumeUserSignUpQueue() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeUserSignUpQueue_withInvalidUserEvent_shouldReject() {
        // Given
        UserEventDTO userEvent = new UserEventDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handleUserEvent(userEvent, UserEventType.SIGNUP);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeUserSignUpQueue(userEvent));

        verify(messagingService).handleUserEvent(userEvent, UserEventType.SIGNUP);
    }

    @Test
    @DisplayName("consumeUserLoginQueue() should process valid user event successfully")
    void consumeUserLoginQueue_withValidUserEvent_shouldProcessSuccessfully() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "LOGIN", 2L, "jane_doe", "jane@example.com",
                Instant.now(), "192.168.1.2"
        );

        // When
        assertDoesNotThrow(() -> listener.consumeUserLoginQueue(userEvent));

        // Then
        verify(messagingService).handleUserEvent(userEvent, UserEventType.LOGIN);
    }

    @Test
    @DisplayName("consumeUserLoginQueue() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeUserLoginQueue_withInvalidUserEvent_shouldReject() {
        // Given
        UserEventDTO userEvent = new UserEventDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handleUserEvent(userEvent, UserEventType.LOGIN);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeUserLoginQueue(userEvent));

        verify(messagingService).handleUserEvent(userEvent, UserEventType.LOGIN);
    }

    @Test
    @DisplayName("consumeSystemErrorQueue() should process valid system event successfully")
    void consumeSystemErrorQueue_withValidSystemEvent_shouldProcessSuccessfully() {
        // Given
        SystemEventDTO systemEvent = new SystemEventDTO(
                "AUTH_SERVICE", "ERROR", "DB_001",
                "Database connection failed",
                Instant.now(), Map.of("host", "db-server")
        );

        // When
        assertDoesNotThrow(() -> listener.consumeSystemErrorQueue(systemEvent));

        // Then
        verify(messagingService).handleSystemErrorMessage(systemEvent);
    }

    @Test
    @DisplayName("consumeSystemErrorQueue() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeSystemErrorQueue_withInvalidSystemEvent_shouldReject() {
        // Given
        SystemEventDTO systemEvent = new SystemEventDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handleSystemErrorMessage(systemEvent);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeSystemErrorQueue(systemEvent));

        verify(messagingService).handleSystemErrorMessage(systemEvent);
    }

    // HEADERS EXCHANGE TESTS

    @Test
    @DisplayName("consumeHighPriorityMessage() should process valid priority message successfully")
    void consumeHighPriorityMessage_withValidMessage_shouldProcessSuccessfully() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "ORDER_SERVICE", "Urgent order",
                "BUSINESS", Instant.now()
        );

        // When
        assertDoesNotThrow(() -> listener.consumeHighPriorityMessage(message));

        // Then
        verify(messagingService).handlePriorityMessage(message, ChannelType.HIGH);
    }

    @Test
    @DisplayName("consumeHighPriorityMessage() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeHighPriorityMessage_withInvalidMessage_shouldReject() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handlePriorityMessage(message, ChannelType.HIGH);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeHighPriorityMessage(message));

        verify(messagingService).handlePriorityMessage(message, ChannelType.HIGH);
    }

    @Test
    @DisplayName("consumeLowPriorityMessage() should process valid priority message successfully")
    void consumeLowPriorityMessage_withValidMessage_shouldProcessSuccessfully() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "NOTIFICATION_SERVICE", "Regular task",
                "SYSTEM", Instant.now()
        );

        // When
        assertDoesNotThrow(() -> listener.consumeLowPriorityMessage(message));

        // Then
        verify(messagingService).handlePriorityMessage(message, ChannelType.LOW);
    }

    @Test
    @DisplayName("consumeLowPriorityMessage() should throw AmqpRejectAndDontRequeueException on failure")
    void consumeLowPriorityMessage_withInvalidMessage_shouldReject() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO();
        doThrow(new IllegalArgumentException())
                .when(messagingService).handlePriorityMessage(message, ChannelType.LOW);

        // When & Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                () -> listener.consumeLowPriorityMessage(message));

        verify(messagingService).handlePriorityMessage(message, ChannelType.LOW);
    }

    // INTEGRATION TEST

    @Test
    @DisplayName("All listener methods should wrap IllegalArgumentException in AmqpRejectAndDontRequeueException")
    void allListenerMethods_shouldWrapIllegalArgumentExceptionInAmqpReject() {
        // Given
        OrderDTO order = new OrderDTO();
        NotificationDTO notification = new NotificationDTO();
        UserEventDTO userEvent = new UserEventDTO();
        SystemEventDTO systemEvent = new SystemEventDTO();
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();

        doThrow(new IllegalArgumentException()).when(messagingService).handleOrder(order);
        doThrow(new IllegalArgumentException()).when(messagingService).handleNotification(any(), any());
        doThrow(new IllegalArgumentException()).when(messagingService).handleUserEvent(any(), any());
        doThrow(new IllegalArgumentException()).when(messagingService).handleSystemErrorMessage(any());
        doThrow(new IllegalArgumentException()).when(messagingService).handlePriorityMessage(any(), any());

        // When & Then - All should throw AmqpRejectAndDontRequeueException
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeOrder(order));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeNotification(notification));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeEmailNotification(notification));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeSmsNotification(notification));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumePushNotification(notification));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeUserSignUpQueue(userEvent));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeUserLoginQueue(userEvent));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeSystemErrorQueue(systemEvent));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeHighPriorityMessage(priorityMessage));
        assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.consumeLowPriorityMessage(priorityMessage));
    }
}
