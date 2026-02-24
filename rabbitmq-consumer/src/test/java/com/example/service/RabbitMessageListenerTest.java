package com.example.service;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RabbitMessageListener Unit Tests")
public class RabbitMessageListenerTest {

    @Mock
    private MessageValidator messageValidator;

    private RabbitMessageListener listener;

    @BeforeEach
    void setUp(){
        listener = new RabbitMessageListener(messageValidator);
    }

    // DIRECT EXCHANGE

    @Test
    @DisplayName("Should process order successfully")
    public void consumeOrder_withValidOrder_shouldProcessSuccessfully(){
        OrderDTO order = new OrderDTO(1L, "Display", 10);

        // When
       assertDoesNotThrow(()->listener.consumeOrder(order));

        // Then
        verify(messageValidator, times(1)).validateOrder(order);
    }

    @Test
    @DisplayName("Should reject order when validation fails")
    public void consumeOrder_withInvalidOrder_shouldReject(){
        //  Given
        OrderDTO order = new OrderDTO(null, "", 0);
        IllegalArgumentException iae = new IllegalArgumentException("Invalid order");

        doThrow(iae).
                when(messageValidator).
                validateOrder(order);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeOrder(order));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateOrder(order);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeOrder_unexpected_shouldRethrow(){
        OrderDTO order = new OrderDTO(null, "", 0);

        doThrow(new RuntimeException()).
                when(messageValidator).
                validateOrder(order);

        assertThrows(RuntimeException.class,
                ()->listener.consumeOrder(order));

        verify(messageValidator).validateOrder(order);
    }


    @Test
    @DisplayName("Should process notification successfully")
    public void consumeNotification_withValidNotification_shouldProcessSuccessfully(){
        NotificationDTO notification = new NotificationDTO();

        // When
        assertDoesNotThrow(()->listener.consumeNotification(notification));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
    }

    @Test
    @DisplayName("Should reject notification when validation fails")
    public void consumeNotification_withInvalidNotification_shouldReject(){
        //  Given
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        IllegalArgumentException iae = new IllegalArgumentException("Invalid notification");


        doThrow(iae).
                when(messageValidator).
                validateNotification(notification);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeNotification(notification));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());

        verify(messageValidator).validateNotification(notification);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeNotification_unexpected_shouldRethrow(){
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        doThrow(new RuntimeException()).
                when(messageValidator).
                validateNotification(notification);

        assertThrows(RuntimeException.class,
                ()->listener.consumeNotification(notification));

        verify(messageValidator).validateNotification(notification);
    }

    // FANOUT EXCHANGE

    @Test
    @DisplayName("Should process email notification successfully")
    public void consumeEmailNotification_withValidEmailNotification_shouldProcessSuccessfully(){
        NotificationDTO notification = new NotificationDTO();

        // When
        assertDoesNotThrow(()->listener.consumeEmailNotification(notification));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
    }

    @Test
    @DisplayName("Should reject email notification when validation fails")
    public void consumeEmailNotification_withInvalidEmailNotification_shouldReject(){
        //  Given
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?
        IllegalArgumentException iae = new IllegalArgumentException("Invalid notification");
        //
        doThrow(iae).
                when(messageValidator).
                validateNotification(notification);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeEmailNotification(notification));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateNotification(notification);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeEmailNotification_unexpected_shouldRethrow(){
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        doThrow(new RuntimeException()).
                when(messageValidator).
                validateNotification(notification);

        assertThrows(RuntimeException.class,
                ()->listener.consumeEmailNotification(notification));

        verify(messageValidator).validateNotification(notification);
    }

    @Test
    @DisplayName("Should process sms notification successfully")
    public void consumeSmsNotification_withValidSmsNotification_shouldProcessSuccessfully(){
        NotificationDTO notification = new NotificationDTO();

        // When
        assertDoesNotThrow(()->listener.consumeSmsNotification(notification));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
    }

    @Test
    @DisplayName("Should reject sms notification when validation fails")
    public void consumeSmsNotification_withInvalidSmsNotification_shouldReject(){
        //  Given
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        IllegalArgumentException iae = new IllegalArgumentException("Invalid notification");

        doThrow(iae).
                when(messageValidator).
                validateNotification(notification);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeSmsNotification(notification));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateNotification(notification);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeSmsNotification_unexpected_shouldRethrow(){
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        doThrow(new RuntimeException()).
                when(messageValidator).
                validateNotification(notification);

        assertThrows(RuntimeException.class,
                ()->listener.consumeSmsNotification(notification));

        verify(messageValidator).validateNotification(notification);
    }

    @Test
    @DisplayName("Should process push notification successfully")
    public void consumePushNotification_withValidPushNotification_shouldProcessSuccessfully(){
        NotificationDTO notification = new NotificationDTO();

        // When
        assertDoesNotThrow(()->listener.consumePushNotification(notification));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
    }

    @Test
    @DisplayName("Should reject push notification when validation fails")
    public void consumePushNotification_withInvalidPushNotification_shouldReject(){
        //  Given
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        IllegalArgumentException iae = new IllegalArgumentException("Invalid notification");

        doThrow(iae).
                when(messageValidator).
                validateNotification(notification);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumePushNotification(notification));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateNotification(notification);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumePushNotification_unexpected_shouldRethrow(){
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        RuntimeException exception = new RuntimeException("Unexpected error");
        doThrow(exception).
                when(messageValidator).
                validateNotification(notification);

        RuntimeException thrown =
        assertThrows(RuntimeException.class,
                ()->listener.consumePushNotification(notification));

        assertEquals(exception, thrown);
        verify(messageValidator).validateNotification(notification);
    }

    // TOPIC EXCHANGE

    @Test
    @DisplayName("Should process user sign up queue successfully")
    public void consumeUserSignUpQueue_withValidUserEvent_shouldProcessSuccessfully(){
        UserEventDTO userEvent = new UserEventDTO();

        // When
        assertDoesNotThrow(()->listener.consumeUserSignUpQueue(userEvent));

        // Then
        verify(messageValidator, times(1)).validateUserEvent(userEvent);
    }

    @Test
    @DisplayName("Should reject user sign up queue when validation fails")
    public void consumeUserSignUpQueue_withInvalidUserEvent_shouldReject(){
        //  Given
        UserEventDTO userEvent = new UserEventDTO();// TODO should i make parameters?

        IllegalArgumentException iae = new IllegalArgumentException("Invalid user event");

        doThrow(iae).
                when(messageValidator).
                validateUserEvent(userEvent);

        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeUserSignUpQueue(userEvent));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateUserEvent(userEvent);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeUserSignUpQueue_unexpected_shouldRethrow(){
        UserEventDTO userEvent = new UserEventDTO();// TODO should i make parameters?
        RuntimeException exception = new RuntimeException("Unexpected error");
        doThrow(exception).
                when(messageValidator).
                validateUserEvent(userEvent);

        RuntimeException thrown =
        assertThrows(RuntimeException.class,
                ()->listener.consumeUserSignUpQueue(userEvent));

        assertSame(exception, thrown);
        verify(messageValidator).validateUserEvent(userEvent);
    }

    // user.login.queue
    @Test
    @DisplayName("Should process user login event successfully")
    public void consumeUserLoginQueue_withValidUserEvent_shouldProcessSuccessfully(){
        UserEventDTO userEvent = new UserEventDTO();

        // When
        assertDoesNotThrow(()->
                listener.consumeUserLoginQueue(userEvent));

        // Then
        verify(messageValidator, times(1)).validateUserEvent(userEvent);
    }

    @Test
    @DisplayName("Should reject user login event when validation fails")
    public void consumeUserLoginQueue_withInvalidUserEvent_shouldReject(){
        //  Given
        UserEventDTO userEvent = new UserEventDTO();// TODO should i make parameters?

        IllegalArgumentException iae = new IllegalArgumentException("Invalid user event");

        doThrow(iae).
                when(messageValidator).
                validateUserEvent(userEvent);

        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeUserLoginQueue(userEvent));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateUserEvent(userEvent);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeUserLoginQueue_unexpected_shouldRethrow(){
        UserEventDTO userEvent = new UserEventDTO();// TODO should i make parameters?

        RuntimeException exception = new RuntimeException("Unexpected exception");
        doThrow(exception).
                when(messageValidator).
                validateUserEvent(userEvent);

        RuntimeException thrown =
        assertThrows(RuntimeException.class,
                ()->listener.consumeUserLoginQueue(userEvent));

        assertEquals(exception, thrown);
        verify(messageValidator).validateUserEvent(userEvent);
    }

    // system.error.queue
    @Test
    @DisplayName("Should process system event successfully")
    public void consumeSystemErrorQueue_withValidSystemEvent_shouldProcessSuccessfully(){
        SystemEventDTO systemEvent = new SystemEventDTO();

        // When
        assertDoesNotThrow(()->
                listener.consumeSystemErrorQueue(systemEvent));

        // Then
        verify(messageValidator, times(1)).validateSystemErrorEvent(systemEvent);
    }

    @Test
    @DisplayName("Should reject system event when validation fails")
    public void consumeSystemErrorQueue_withInvalidSystemEvent_shouldReject(){
        //  Given
        SystemEventDTO systemEvent = new SystemEventDTO();// TODO should i make parameters?

        IllegalArgumentException iae = new IllegalArgumentException("Invalid system event");

        doThrow(iae).
                when(messageValidator).
                validateSystemErrorEvent(systemEvent);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeSystemErrorQueue(systemEvent));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validateSystemErrorEvent(systemEvent);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeSystemErrorQueue_unexpected_shouldRethrow(){
        SystemEventDTO systemEvent = new SystemEventDTO();// TODO should i make parameters?

        RuntimeException exception = new RuntimeException("Unexpected exception");
        doThrow(exception).
                when(messageValidator).
                validateSystemErrorEvent(systemEvent);

        RuntimeException thrown =
        assertThrows(RuntimeException.class,
                ()->listener.consumeSystemErrorQueue(systemEvent));

        assertEquals(exception, thrown);
        verify(messageValidator).validateSystemErrorEvent(systemEvent);
    }

    // HEADERS EXCHANGE

    // high.priority.queue
    @Test
    @DisplayName("Should process high priority message successfully")
    public void consumeHighPriorityMessage_withValidPriority_shouldProcessSuccessfully(){
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();

        // When
        assertDoesNotThrow(()->
                listener.consumeHighPriorityMessage(priorityMessage));

        // Then
        verify(messageValidator, times(1)).validatePriorityMessage(priorityMessage);

    }

    @Test
    @DisplayName("Should reject priority message when validation fails")
    public void consumeHighPriorityMessage_withInvalidPriorityMessage_shouldReject(){
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();

        IllegalArgumentException iae = new IllegalArgumentException("Invalid priority message");

        doThrow(iae).
                when(messageValidator).
                validatePriorityMessage(priorityMessage);

        // When
        AmqpRejectAndDontRequeueException exception =
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()-> listener.consumeHighPriorityMessage(priorityMessage));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validatePriorityMessage(priorityMessage);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeHighPriorityMessage_unexpected_shouldRethrow(){
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();// TODO should i make parameters?

        RuntimeException exception = new RuntimeException("Unexpected error");

        doThrow(exception).
                when(messageValidator).
                validatePriorityMessage(priorityMessage);

        RuntimeException thrown =
        assertThrows(RuntimeException.class,
                ()->listener.consumeHighPriorityMessage(priorityMessage));

        assertEquals(exception, thrown);
        verify(messageValidator).validatePriorityMessage(priorityMessage);
    }

    // low.priority.queue
    @Test
    @DisplayName("Should process low priority message successfully")
    public void consumeLowPriorityMessage_withValidPriority_shouldProcessSuccessfully(){
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();

        assertDoesNotThrow(()->
                listener.consumeLowPriorityMessage(priorityMessage));

        verify(messageValidator, times(1)).validatePriorityMessage(priorityMessage);
    }

    @Test
    @DisplayName("Should reject priority message when validation fails")
    public void consumeLowPriorityMessage_withInvalidPriorityMessage_shouldReject(){
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();

        IllegalArgumentException iae = new IllegalArgumentException("Priority must not be null");

        doThrow(iae).
                when(messageValidator).
                validatePriorityMessage(priorityMessage);

        // When
        AmqpRejectAndDontRequeueException exception = assertThrows(AmqpRejectAndDontRequeueException.class,
                ()-> listener.consumeLowPriorityMessage(priorityMessage));

        // Then
        assertEquals("Invalid message payload", exception.getMessage());
        assertSame(iae, exception.getCause());
        verify(messageValidator).validatePriorityMessage(priorityMessage);
    }

    @Test
    @DisplayName("Should rethrow unexpected exception")
    public void consumeLowPriorityMessage_unexpected_shouldRethrow(){
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO();

        RuntimeException exception = new RuntimeException("Unexpected error");

        doThrow(exception)
                .when(messageValidator)
                .validatePriorityMessage(priorityMessage);

        RuntimeException thrown =
        assertThrows(RuntimeException.class,
                ()->listener.consumeLowPriorityMessage(priorityMessage));

        assertEquals(exception, thrown);
        verify(messageValidator).validatePriorityMessage(priorityMessage);
    }

    
//    @Mock
//    private MessageValidator messageValidator;
//
//    @Mock
//    private Validator validator;
//
//    @Captor
//    private ArgumentCaptor<OrderDTO> orderCaptor;
//
//    private RabbitMessageListener messageListener;
//
//    @BeforeEach
//    void setUp() {
//        messageListener = new RabbitMessageListener(messageValidator);
//    }
//
//    @Test
//    @DisplayName("Should throw AmqpRejectAndDontRequeueException when order validation fails")
//    void shouldThrowAmqpExceptionWhenValidationFails() {
//        // Given
//        OrderDTO invalidOrder = new OrderDTO(null, "", 0);
//        String validationError = "Invalid Order: Order ID cannot be null, Product name is required, Quantity must be at least 1";
//
//        doThrow(new IllegalArgumentException(validationError))
//            .when(messageValidator).validateOrder(any(OrderDTO.class));
//
//        // When & Then
//        AmqpRejectAndDontRequeueException exception = assertThrows(
//            AmqpRejectAndDontRequeueException.class,
//            () -> messageListener.consumeOrder(invalidOrder)
//        );
//
//        assertEquals("Invalid message payload", exception.getMessage());
//        assertTrue(exception.getCause() instanceof IllegalArgumentException);
//        assertEquals(validationError, exception.getCause().getMessage());
//
//        verify(messageValidator, times(1)).validateOrder(orderCaptor.capture());
//        assertEquals(invalidOrder, orderCaptor.getValue());
//    }
//
//    @Test
//    @DisplayName("Should propagate exception when unexpected error occurs")
//    void shouldPropagateExceptionWhenUnexpectedErrorOccurs() {
//        // Given
//        OrderDTO order = new OrderDTO(1L, "Laptop", 2);
//        RuntimeException unexpectedError = new RuntimeException("Database connection failed");
//
//        doThrow(unexpectedError)
//            .when(messageValidator).validateOrder(any(OrderDTO.class));
//
//        // When & Then
//        RuntimeException exception = assertThrows(
//            RuntimeException.class,
//            () -> messageListener.consumeOrder(order)
//        );
//
//        assertEquals("Database connection failed", exception.getMessage());
//        assertSame(unexpectedError, exception);
//
//        verify(messageValidator, times(1)).validateOrder(order);
//    }
//
//    @Test
//    @DisplayName("Should handle null order gracefully")
//    void shouldHandleNullOrder() {
//        // Given
//        doThrow(new IllegalArgumentException("Ordercannot be null"))
//            .when(messageValidator).validateOrder(isNull());
//
//        // When & Then
//        AmqpRejectAndDontRequeueException exception = assertThrows(
//            AmqpRejectAndDontRequeueException.class,
//            () -> messageListener.consumeOrder(null)
//        );
//
//        assertEquals("Invalid message payload", exception.getMessage());
//        assertTrue(exception.getCause() instanceof IllegalArgumentException);
//
//        verify(messageValidator, times(1)).validateOrder(isNull());
//    }
//
//    @Test
//    @DisplayName("Should process order with minimum valid quantity")
//    void shouldProcessOrderWithMinimumValidQuantity() {
//        // Given
//        OrderDTO order = new OrderDTO(1L, "Mouse", 1);
//
//        // When
//        assertDoesNotThrow(() -> messageListener.consumeOrder(order));
//
//        // Then
//        verify(messageValidator, times(1)).validateOrder(order);
//    }
//
//    @Test
//    @DisplayName("Should process order with large quantity")
//    void shouldProcessOrderWithLargeQuantity() {
//        // Given
//        OrderDTO order = new OrderDTO(1L, "Keyboard", 1000);
//
//        // When
//        assertDoesNotThrow(() -> messageListener.consumeOrder(order));
//
//        // Then
//        verify(messageValidator, times(1)).validateOrder(order);
//    }
}
