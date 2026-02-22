package com.example.service;

import com.example.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RabbitMessagePublisher Unit Tests")
class RabbitMessagePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMessagePublisher publisher;

    @Captor
    private ArgumentCaptor<String> exchangeCaptor;

    @Captor
    private ArgumentCaptor<String> routingKeyCaptor;

    @Captor
    private ArgumentCaptor<Object> messageCaptor;

    @Captor
    private ArgumentCaptor<CorrelationData> correlationDataCaptor;

    private static final String DIRECT_EXCHANGE = "direct.exchange";
    private static final String FANOUT_EXCHANGE = "fanout.exchange";
    private static final String TOPIC_EXCHANGE = "topic.exchange";
    private static final String HEADERS_EXCHANGE = "headers.exchange";

    private static final String ORDERS_ROUTE = "orders.route";
    private static final String NOTIFICATION_ROUTE = "notification.route";
    private static final String USER_SIGNUP_ROUTE = "user.signup.route";
    private static final String USER_LOGIN_ROUTE = "user.login.route";
    private static final String SYSTEM_ERROR_ROUTE = "system.error.route";

    @BeforeEach
    void setUp() {
        // Initialize publisher with test values using reflection
        publisher = new RabbitMessagePublisher(
                rabbitTemplate,
                DIRECT_EXCHANGE,
                FANOUT_EXCHANGE,
                TOPIC_EXCHANGE,
                HEADERS_EXCHANGE
        );

        // Set routing keys via reflection (since they are @Value injected)
        ReflectionTestUtils.setField(publisher, "ordersRoute", ORDERS_ROUTE);
        ReflectionTestUtils.setField(publisher, "notificationRoute", NOTIFICATION_ROUTE);
        ReflectionTestUtils.setField(publisher, "userSignup", USER_SIGNUP_ROUTE);
        ReflectionTestUtils.setField(publisher, "userLogin", USER_LOGIN_ROUTE);
        ReflectionTestUtils.setField(publisher, "systemError", SYSTEM_ERROR_ROUTE);
    }

    // DIRECT EXCHANGE TESTS

    @Test
    @DisplayName("sendOrder() should send message to direct exchange with correct routing key")
    void sendOrder_shouldSendMessageToDirectExchange() {
        // Given
        OrderDTO order = new OrderDTO(1L, "Laptop", 2);

        // When
        publisher.sendOrder(order);

        // Then
        verify(rabbitTemplate, times(1)).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                messageCaptor.capture(),
                correlationDataCaptor.capture()
        );

        assertThat(exchangeCaptor.getValue()).isEqualTo(DIRECT_EXCHANGE);
        assertThat(routingKeyCaptor.getValue()).isEqualTo(ORDERS_ROUTE);
        assertThat(messageCaptor.getValue()).isEqualTo(order);
        assertThat(correlationDataCaptor.getValue().getId()).isNotNull();
    }

    @Test
    @DisplayName("sendOrder() should generate unique CorrelationData ID")
    void sendOrder_shouldGenerateUniqueCorrelationDataId() {
        // Given
        OrderDTO order = new OrderDTO(1L, "Mouse", 5);

        // When
        publisher.sendOrder(order);
        publisher.sendOrder(order);

        // Then
        verify(rabbitTemplate, times(2)).convertAndSend(
                anyString(),
                anyString(),
                any(OrderDTO.class),
                correlationDataCaptor.capture()
        );

        String firstId = correlationDataCaptor.getAllValues().get(0).getId();
        String secondId = correlationDataCaptor.getAllValues().get(1).getId();

        assertThat(firstId).isNotEqualTo(secondId);
    }

    @Test
    @DisplayName("sendNotification() should send message to direct exchange")
    void sendNotification_shouldSendMessageToDirectExchange() {
        // Given
        NotificationDTO notification = new NotificationDTO(100L, "Welcome!");

        // When
        publisher.sendNotification(notification);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(DIRECT_EXCHANGE),
                eq(NOTIFICATION_ROUTE),
                eq(notification),
                any(CorrelationData.class)
        );
    }

    // FANOUT EXCHANGE TESTS

    @Test
    @DisplayName("sendFanoutNotification() should send message to fanout exchange with empty routing key")
    void sendFanoutNotification_shouldSendToFanoutExchange() {
        // Given
        NotificationDTO notification = new NotificationDTO(200L, "System update");

        // When
        publisher.sendFanoutNotification(notification);

        // Then
        verify(rabbitTemplate).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                messageCaptor.capture(),
                correlationDataCaptor.capture()
        );

        assertThat(exchangeCaptor.getValue()).isEqualTo(FANOUT_EXCHANGE);
        assertThat(routingKeyCaptor.getValue()).isEmpty();
        assertThat(messageCaptor.getValue()).isEqualTo(notification);
        assertThat(correlationDataCaptor.getValue()).isNotNull();
    }

    @Test
    @DisplayName("sendFanoutNotification() should create CorrelationData")
    void sendFanoutNotification_shouldCreateCorrelationData() {
        // Given
        NotificationDTO notification = new NotificationDTO(300L, "Alert");

        // When
        publisher.sendFanoutNotification(notification);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(FANOUT_EXCHANGE),
                eq(""),
                eq(notification),
                correlationDataCaptor.capture()
        );

        assertThat(correlationDataCaptor.getValue().getId()).isNotNull();
    }

    // TOPIC EXCHANGE TESTS

    @Test
    @DisplayName("sendUserSignup() should send message to topic exchange with signup routing key")
    void sendUserSignup_shouldSendToTopicExchange() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "SIGNUP",
                1L,
                "john_doe",
                "john@example.com",
                java.time.Instant.now(),
                "192.168.1.1"
        );

        // When
        publisher.sendUserSignup(userEvent);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(TOPIC_EXCHANGE),
                eq(USER_SIGNUP_ROUTE),
                eq(userEvent),
                any(CorrelationData.class)
        );
    }

    @Test
    @DisplayName("sendUserLogin() should send message to topic exchange with login routing key")
    void sendUserLogin_shouldSendToTopicExchange() {
        // Given
        UserEventDTO userEvent = new UserEventDTO(
                "LOGIN",
                2L,
                "jane_doe",
                "jane@example.com",
                java.time.Instant.now(),
                "192.168.1.2"
        );

        // When
        publisher.sendUserLogin(userEvent);

        // Then
        verify(rabbitTemplate).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                messageCaptor.capture(),
                any(CorrelationData.class)
        );

        assertThat(exchangeCaptor.getValue()).isEqualTo(TOPIC_EXCHANGE);
        assertThat(routingKeyCaptor.getValue()).isEqualTo(USER_LOGIN_ROUTE);
        assertThat(messageCaptor.getValue()).isEqualTo(userEvent);
    }

    @Test
    @DisplayName("sendSystemError() should send message to topic exchange with error routing key")
    void sendSystemError_shouldSendToTopicExchange() {
        // Given
        SystemEventDTO systemEvent = new SystemEventDTO(
                "AUTH_SERVICE",
                "ERROR",
                "DB_001",
                "Database connection failed",
                java.time.Instant.now(),
                java.util.Map.of("host", "db-server", "port", "5432")
        );

        // When
        publisher.sendSystemError(systemEvent);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(TOPIC_EXCHANGE),
                eq(SYSTEM_ERROR_ROUTE),
                eq(systemEvent),
                any(CorrelationData.class)
        );
    }

    @Test
    @DisplayName("sendSystemError() should generate CorrelationData with UUID")
    void sendSystemError_shouldGenerateCorrelationDataWithUuid() {
        // Given
        SystemEventDTO systemEvent = new SystemEventDTO(
                "PAYMENT_SERVICE",
                "WARNING",
                "NET_002",
                "Network timeout",
                java.time.Instant.now(),
                java.util.Map.of("timeout", "30s")
        );

        // When
        publisher.sendSystemError(systemEvent);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(TOPIC_EXCHANGE),
                eq(SYSTEM_ERROR_ROUTE),
                eq(systemEvent),
                correlationDataCaptor.capture()
        );

        String correlationId = correlationDataCaptor.getValue().getId();
        assertThat(correlationId).isNotNull();
        assertThat(correlationId).matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }

    // HEADERS EXCHANGE TESTS

    @Test
    @DisplayName("sendPriorityMessage() should send message to headers exchange with high priority")
    void sendPriorityMessage_shouldSendWithHighPriority() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "ORDER_SERVICE",
                "Urgent order processing required",
                "BUSINESS",
                java.time.Instant.now()
        );
        String priority = "HIGH";

        // When
        publisher.sendPriorityMessage(message, priority);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(HEADERS_EXCHANGE),
                eq(""),
                eq(message),
                any(MessagePostProcessor.class),
                any(CorrelationData.class)
        );
    }

    @Test
    @DisplayName("sendPriorityMessage() should send message to headers exchange with low priority")
    void sendPriorityMessage_shouldSendWithLowPriority() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "NOTIFICATION_SERVICE",
                "Regular notification task",
                "SYSTEM",
                java.time.Instant.now()
        );
        String priority = "LOW";

        // When
        publisher.sendPriorityMessage(message, priority);

        // Then
        verify(rabbitTemplate).convertAndSend(
                exchangeCaptor.capture(),
                routingKeyCaptor.capture(),
                messageCaptor.capture(),
                any(MessagePostProcessor.class),
                correlationDataCaptor.capture()
        );

        assertThat(exchangeCaptor.getValue()).isEqualTo(HEADERS_EXCHANGE);
        assertThat(routingKeyCaptor.getValue()).isEmpty();
        assertThat(messageCaptor.getValue()).isEqualTo(message);
        assertThat(correlationDataCaptor.getValue()).isNotNull();
    }

    @Test
    @DisplayName("sendPriorityMessage() should use MessagePostProcessor")
    void sendPriorityMessage_shouldUseMessagePostProcessor() {
        // Given
        PriorityMessageDTO message = new PriorityMessageDTO(
                "ANALYTICS_SERVICE",
                "Process analytics data",
                "BUSINESS",
                java.time.Instant.now()
        );

        // When
        publisher.sendPriorityMessage(message, "MEDIUM");

        // Then
        verify(rabbitTemplate).convertAndSend(
                anyString(),
                anyString(),
                any(),
                any(MessagePostProcessor.class),
                any(CorrelationData.class)
        );
    }

    // EDGE CASE TESTS

    @Test
    @DisplayName("sendOrder() with null DTO should not throw exception (validation handled by @Valid)")
    void sendOrder_withNullDto_shouldInvokeRabbitTemplate() {
        // Given
        OrderDTO order = null;

        // When
        publisher.sendOrder(order);

        // Then
        verify(rabbitTemplate).convertAndSend(
                eq(DIRECT_EXCHANGE),
                eq(ORDERS_ROUTE),
                isNull(OrderDTO.class),
                any(CorrelationData.class)
        );
    }


    @Test
    @DisplayName("Multiple message sends should create different CorrelationData IDs")
    void multipleSends_shouldCreateDifferentCorrelationIds() {
        // Given
        NotificationDTO notification1 = new NotificationDTO(1L, "Message 1");
        NotificationDTO notification2 = new NotificationDTO(2L, "Message 2");
        NotificationDTO notification3 = new NotificationDTO(3L, "Message 3");

        // When
        publisher.sendNotification(notification1);
        publisher.sendNotification(notification2);
        publisher.sendNotification(notification3);

        // Then
        verify(rabbitTemplate, times(3)).convertAndSend(
                anyString(),
                anyString(),
                any(NotificationDTO.class),
                correlationDataCaptor.capture()
        );

        var ids = correlationDataCaptor.getAllValues().stream()
                .map(CorrelationData::getId)
                .toList();

        assertThat(ids).hasSize(3);
        assertThat(ids).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("RabbitTemplate should never be called without CorrelationData")
    void allMethods_shouldAlwaysProvideCorrelationData() {
        // Given
        OrderDTO order = new OrderDTO(1L, "Test Product", 1);
        NotificationDTO notification = new NotificationDTO(1L, "Test notification");
        UserEventDTO userEvent = new UserEventDTO(
                "TEST",
                1L,
                "test_user",
                "test@test.com",
                java.time.Instant.now(),
                "127.0.0.1"
        );
        SystemEventDTO systemEvent = new SystemEventDTO(
                "TEST_SERVICE",
                "INFO",
                "TEST_001",
                "Test message",
                java.time.Instant.now(),
                java.util.Map.of("key", "value")
        );
        PriorityMessageDTO priorityMessage = new PriorityMessageDTO(
                "TEST_SERVICE",
                "Test priority message",
                "SYSTEM",
                java.time.Instant.now()
        );

        // When
        publisher.sendOrder(order);
        publisher.sendNotification(notification);
        publisher.sendFanoutNotification(notification);
        publisher.sendUserSignup(userEvent);
        publisher.sendUserLogin(userEvent);
        publisher.sendSystemError(systemEvent);
        publisher.sendPriorityMessage(priorityMessage, "HIGH");

        // Then - verify all 7 calls included CorrelationData
        verify(rabbitTemplate, times(6)).convertAndSend(
                anyString(),
                anyString(),
                any(Object.class),
                any(CorrelationData.class)
        );

        // Priority message uses different signature with MessagePostProcessor
        verify(rabbitTemplate, times(1)).convertAndSend(
                anyString(),
                anyString(),
                any(),
                any(MessagePostProcessor.class),
                any(CorrelationData.class)
        );
    }
}