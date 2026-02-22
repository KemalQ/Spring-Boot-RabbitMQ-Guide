package com.example.service;

import com.example.configuration.MessageValidator;
import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
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
    public void consumeOrder_withValidOrder_shouldProcessSuccessfully(){
        OrderDTO order = new OrderDTO(1L, "Display", 10);

        // When
       assertDoesNotThrow(()->listener.consumeOrder(order));

        // Then
        verify(messageValidator, times(1)).validateOrder(order);
    }

    @Test
    public void consumeOrder_withInvalidOrder_shouldReject(){
        //  Given
        OrderDTO order = new OrderDTO(null, "", 0);

        // When
        doThrow(new IllegalArgumentException()).
                when(messageValidator).
                validateOrder(order);

        // Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeOrder(order));

        verify(messageValidator).validateOrder(order);
    }

    @Test
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
    public void consumeNotification_withValidNotification_shouldProcessSuccessfully(){
        NotificationDTO notification = new NotificationDTO();

        // When
        assertDoesNotThrow(()->listener.consumeNotification(notification));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
    }

    @Test
    public void consumeNotification_withInvalidNotification_shouldReject(){
        //  Given
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        // When
        doThrow(new IllegalArgumentException()).
                when(messageValidator).
                validateNotification(notification);

        // Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeNotification(notification));

        verify(messageValidator).validateNotification(notification);
    }

    @Test
    public void consumeNotification_unexpected_shouldRethrow(){
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        doThrow(new RuntimeException()).
                when(messageValidator).
                validateNotification(notification);

        assertThrows(RuntimeException.class,
                ()->listener.consumeNotification(notification));

        verify(messageValidator).validateNotification(notification);
    }

    @Test
    public void consumeEmailNotification_withValidEmailNotification_shouldProcessSuccessfully(){
        NotificationDTO notification = new NotificationDTO();

        // When
        assertDoesNotThrow(()->listener.consumeEmailNotification(notification));

        // Then
        verify(messageValidator, times(1)).validateNotification(notification);
    }

    @Test
    public void consumeEmailNotification_withInvalidEmailNotification_shouldReject(){
        //  Given
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        // When
        doThrow(new IllegalArgumentException()).
                when(messageValidator).
                validateNotification(notification);

        // Then
        assertThrows(AmqpRejectAndDontRequeueException.class,
                ()->listener.consumeEmailNotification(notification));

        verify(messageValidator).validateNotification(notification);
    }

    @Test
    public void consumeEmailNotification_unexpected_shouldRethrow(){
        NotificationDTO notification = new NotificationDTO();// TODO should i make parameters?

        doThrow(new RuntimeException()).
                when(messageValidator).
                validateNotification(notification);

        assertThrows(RuntimeException.class,
                ()->listener.consumeEmailNotification(notification));

        verify(messageValidator).validateNotification(notification);
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
