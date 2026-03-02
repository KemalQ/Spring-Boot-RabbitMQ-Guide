# 🐰 Spring Boot RabbitMQ Microservices

> Asynchronous messaging system demonstrating enterprise patterns with RabbitMQ

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-4.1-FF6600?style=flat&logo=rabbitmq)](https://www.rabbitmq.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=flat&logo=docker)](https://www.docker.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=flat&logo=apache-maven)](https://maven.apache.org/)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Features](#-features)
- [Technologies](#-technologies)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Exchange Types](#-exchange-types)
- [API Documentation](#-api-documentation)
- [Configuration](#-configuration)
- [Production Features](#-production-features)
- [Testing](#-testing)
- [Project Structure](#-project-structure)

---

## 🎯 Overview

This project demonstrates a **microservices architecture** using **Spring Boot** and **RabbitMQ** for asynchronous message-driven communication. It implements all four RabbitMQ exchange patterns with comprehensive error handling, publisher confirms, and validation.

### What Makes This Production-Ready?

✅ **Publisher Confirms & Returns** - Guaranteed message delivery tracking  
✅ **Comprehensive Validation** - Jakarta Validation on DTOs with custom validators  
✅ **Error Handling** - AmqpRejectAndDontRequeueException for invalid payloads  
✅ **Health Checks** - Spring Actuator endpoints for monitoring  
✅ **Docker Ready** - Complete docker-compose setup with health checks  
✅ **All 4 Exchange Types** - Direct, Fanout, Topic, and Headers patterns

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              CLIENT REQUESTS                             │
└──────────────────────────────┬──────────────────────────────────────────┘
                               │
                               ↓
        ┌──────────────────────────────────────────────┐
        │     PRODUCER MICROSERVICE (Port 8080)        │
        │  ┌────────────────────────────────────────┐  │
        │  │  REST Controllers                      │  │
        │  │  • DirectController                    │  │
        │  │  • FanoutController                    │  │
        │  │  • TopicController                     │  │
        │  │  • HeadersController                   │  │
        │  └──────────────┬─────────────────────────┘  │
        │                 │                            │
        │  ┌──────────────▼─────────────────────────┐  │
        │  │  RabbitMessagePublisher                │  │
        │  │  • CorrelationData tracking            │  │
        │  │  • DTO validation (@Valid)             │  │
        │  └──────────────┬─────────────────────────┘  │
        └─────────────────┼──────────────────────────┘
                          │
                          │ Publisher Confirms
                          │ Publisher Returns
                          ↓
        ┌──────────────────────────────────────────────┐
        │         RABBITMQ BROKER (Port 5672)          │
        │  ┌────────────────────────────────────────┐  │
        │  │  EXCHANGES                             │  │
        │  │  • direct.exchange   (1:1 routing)     │  │
        │  │  • fanout.exchange   (broadcast)       │  │
        │  │  • topic.exchange    (pattern routing) │  │
        │  │  • headers.exchange  (header routing)  │  │
        │  └──────────────┬─────────────────────────┘  │
        │                 │                            │
        │  ┌──────────────▼─────────────────────────┐  │
        │  │  QUEUES (Durable, Persistent)          │  │
        │  │  • orders.queue                        │  │
        │  │  • notification.queue                  │  │
        │  │  • email.queue, sms.queue, push.queue  │  │
        │  │  • user.signup.queue, user.login.queue │  │
        │  │  • system.error.queue                  │  │
        │  │  • priority.high.queue, priority.low   │  │
        │  └──────────────┬─────────────────────────┘  │
        └─────────────────┼──────────────────────────┘
                          │
                          │ Message Delivery
                          ↓
        ┌──────────────────────────────────────────────┐
        │     CONSUMER MICROSERVICE (Port 8081)        │
        │  ┌────────────────────────────────────────┐  │
        │  │  RabbitMessageListener                 │  │
        │  │  • @RabbitListener for each queue      │  │
        │  │  • Concurrent processing (2-5 threads) │  │
        │  └──────────────┬─────────────────────────┘  │
        │                 │                            │
        │  ┌──────────────▼─────────────────────────┐  │
        │  │  MessageValidator                      │  │
        │  │  • Jakarta Validation                  │  │
        │  │  • Custom constraint validation        │  │
        │  └──────────────┬─────────────────────────┘  │
        │                 │                            │
        │  ┌──────────────▼─────────────────────────┐  │
        │  │  Business Logic Processing             │  │
        │  │  • Order processing                    │  │
        │  │  • Notification handling               │  │
        │  │  • Event processing                    │  │
        │  └────────────────────────────────────────┘  │
        └──────────────────────────────────────────────┘
```

---

## ✨ Features

### Core Functionality

🎯 **4 Exchange Patterns** - Complete implementation of all RabbitMQ exchange types  
🔄 **Asynchronous Processing** - Non-blocking message-driven architecture  
📦 **Multiple Message Types** - Orders, Notifications, User Events, System Events, Priority Messages  
🎨 **Pattern-Based Routing** - Wildcard routing with Topic Exchange (`user.signup.*`, `system.error.#`)  
🏷️ **Header-Based Routing** - Priority-based message routing with Headers Exchange

### Production Features

✅ **Publisher Confirms** - Broker acknowledges message receipt with CorrelationData tracking  
🔄 **Publisher Returns** - Automatic detection and logging of unroutable messages  
🛡️ **Input Validation** - Jakarta Bean Validation on all DTOs (`@NotNull`, `@NotBlank`, `@Min`)  
🎭 **Exception Handling** - `AmqpRejectAndDontRequeueException` for poison messages  
📊 **Concurrent Consumers** - Configurable consumer threads (2-5) with prefetch control  
💊 **Health Monitoring** - Custom RabbitMQ health indicators with Actuator  
🐳 **Docker Orchestration** - Complete docker-compose with health checks and dependencies

### Message Flow Control

🚫 **No Requeue on Validation Errors** - Invalid messages rejected immediately  
⏰ **Prefetch Count (10)** - Optimized message batching  
🔁 **Auto Acknowledgment** - Configurable ACK mode (AUTO/MANUAL)  
📝 **Structured Logging** - Correlation IDs for message tracking

---

## 🛠️ Technologies

| Technology | Version        | Purpose |
|------------|----------------|---------|
| **Java** | 21             | Programming Language |
| **Spring Boot** | 3.5.5          | Application Framework |
| **Spring AMQP** | 3.1.x          | RabbitMQ Integration |
| **RabbitMQ** | 4.1-management | Message Broker |
| **Jackson** | 2.15.x         | JSON Serialization |
| **Lombok** | 1.18.x         | Boilerplate Reduction |
| **Jakarta Validation** | 3.0.x          | Bean Validation |
| **Docker** | 24.x           | Containerization |
| **Maven** | 3.9.x          | Build Tool |

---

## 📦 Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker** and **Docker Compose**
- **Git**

---

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/KemalQ/Spring-Boot-RabbitMQ-Guide.git
cd Spring-Boot-RabbitMQ-Guide
```

### 2. Start RabbitMQ with Docker Compose

```bash
docker-compose up -d
```

**RabbitMQ Management UI:** http://localhost:15672  
**Default Credentials:** `guest` / `guest`

### 3. Build the Projects

```bash
# Build both microservices
mvn clean install

# Or build individually
cd rabbitmq-producer && mvn clean install
cd ../rabbitmq-consumer && mvn clean install
```

### 4. Run the Microservices

**Terminal 1 - Start Consumer:**
```bash
cd rabbitmq-consumer
mvn spring-boot:run
```

**Terminal 2 - Start Producer:**
```bash
cd rabbitmq-producer
mvn spring-boot:run
```

### 5. Test the Setup

```bash
# Send a test order
curl -X POST "http://localhost:8080/sendOrder" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "product": "Laptop",
    "quantity": 2
  }'
```

**Check Logs:**
- **Producer:** Should show `✅ [CONFIRM] Message acknowledged`
- **Consumer:** Should show `✅ Received message is: product=Laptop, quantity=2`

---

## 🔀 Exchange Types

This project implements all four RabbitMQ exchange patterns:

### 1️⃣ Direct Exchange (Point-to-Point)

**Use Case:** Route messages to specific queues using exact routing keys

**Example:**
```bash
# Send Order
curl -X POST "http://localhost:8080/direct/sendOrder" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 101,
    "product": "MacBook Pro",
    "quantity": 1
  }'

# Send Notification
curl -X POST "http://localhost:8080/direct/notification" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 123,
    "message": "Your order has been shipped!"
  }'
```

**Routing:**
```
direct.exchange
  ├── orders.route      → orders.queue
  └── notification.route → notification.queue
```

---

### 2️⃣ Fanout Exchange (Broadcast)

**Use Case:** Broadcast message to ALL bound queues (publish-subscribe pattern)

**Example:**
```bash
# Broadcast notification to email, SMS, and push notifications
curl -X POST "http://localhost:8080/fanout/notification" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 456,
    "message": "Special promotion: 50% off!"
  }'
```

**Routing:**
```
fanout.exchange
  ├── "" → email.queue
  ├── "" → sms.queue
  └── "" → push.queue
```
*Message delivered to ALL three queues simultaneously*

---

### 3️⃣ Topic Exchange (Pattern-Based Routing)

**Use Case:** Route messages based on wildcard patterns (`*` = one word, `#` = zero or more words)

**Examples:**
```bash
# User Signup Event
curl -X POST "http://localhost:8080/topic/user/signup" \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "SIGNUP",
    "userId": 789,
    "username": "john_doe",
    "email": "john@example.com",
    "occurredAt": "2024-02-15T10:30:00Z",
    "ipAddress": "192.168.1.1"
  }'

# System Error Event
curl -X POST "http://localhost:8080/topic/system/error" \
  -H "Content-Type: application/json" \
  -d '{
    "component": "payment-service",
    "severity": "HIGH",
    "errorCode": "PAY-500",
    "message": "Payment gateway timeout",
    "createdAt": "2024-02-15T10:35:00Z",
    "metadata": {"timeout": "30s"}
  }'
```

**Routing Patterns:**
```
topic.exchange
  ├── user.signup.*      → user.signup.queue   (matches user.signup.web, user.signup.mobile)
  ├── user.login.*       → user.login.queue    (matches user.login.web, user.login.mobile)
  └── system.error.#     → system.error.queue  (matches system.error, system.error.critical, system.error.db.timeout)
```

---

### 4️⃣ Headers Exchange (Header-Based Routing)

**Use Case:** Route messages based on message headers (not routing key)

**Examples:**
```bash
# High Priority Message
curl -X POST "http://localhost:8080/headers/send?priority=high" \
  -H "Content-Type: application/json" \
  -d '{
    "source": "payment-service",
    "message": "Critical: Payment processing failed",
    "level": "SYSTEM",
    "createdAt": "2024-02-15T10:40:00Z"
  }'

# Low Priority Message
curl -X POST "http://localhost:8080/headers/send?priority=low" \
  -H "Content-Type: application/json" \
  -d '{
    "source": "analytics-service",
    "message": "Daily report generated",
    "level": "BUSINESS",
    "createdAt": "2024-02-15T10:45:00Z"
  }'
```

**Routing Rules:**
```
headers.exchange
  ├── priority: high → priority.high.queue
  └── priority: low  → priority.low.queue
```

---

## 📚 API Documentation

### Producer Endpoints (Port 8080)

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/direct/order` | Send order to specific queue | `OrderDTO` |
| `POST` | `/direct/notification` | Send notification to specific queue | `NotificationDTO` |
| `POST` | `/fanout/notification` | Broadcast notification to all queues | `NotificationDTO` |
| `POST` | `/topic/user/signup` | Send user signup event | `UserEventDTO` |
| `POST` | `/topic/user/login` | Send user login event | `UserEventDTO` |
| `POST` | `/topic/system/error` | Send system error event | `SystemEventDTO` |
| `POST` | `/headers/send?priority={value}` | Send priority message | `PriorityMessageDTO` |

### DTOs

**OrderDTO:**
```json
{
  "id": 1,
  "product": "Laptop",
  "quantity": 2
}
```

**NotificationDTO:**
```json
{
  "userId": 123,
  "message": "Your order has been shipped!"
}
```

**UserEventDTO:**
```json
{
  "eventType": "SIGNUP",
  "userId": 789,
  "username": "john_doe",
  "email": "john@example.com",
  "occurredAt": "2024-02-15T10:30:00Z",
  "ipAddress": "192.168.1.1"
}
```

**SystemEventDTO:**
```json
{
  "component": "payment-service",
  "severity": "HIGH",
  "errorCode": "PAY-500",
  "message": "Payment gateway timeout",
  "createdAt": "2024-02-15T10:35:00Z",
  "metadata": {"timeout": "30s"}
}
```

**PriorityMessageDTO:**
```json
{
  "source": "payment-service",
  "message": "Critical alert",
  "level": "SYSTEM",
  "createdAt": "2024-02-15T10:40:00Z"
}
```

### Consumer Health Check (Port 8081)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/actuator/health` | RabbitMQ connection health status |
| `GET` | `/actuator/info` | Application information |

---

## ⚙️ Configuration

### Producer Configuration

`rabbitmq-producer/src/main/resources/application.properties`

```properties
# Server
server.port=8080

# RabbitMQ Connection
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Publisher Confirms & Returns
spring.rabbitmq.publisher-confirm-type=correlated
spring.rabbitmq.publisher-returns=true
spring.rabbitmq.template.mandatory=true

# Exchanges
direct.exchange=direct.exchange
fanout.exchange=fanout.exchange
topic.exchange=topic.exchange
headers.exchange=headers.exchange

# Queues & Routing Keys
orders.queue=orders.queue
orders.route=orders.route
notification.queue=notification.queue
notification.route=notification.route
```

### Consumer Configuration

`rabbitmq-consumer/src/main/resources/application.properties`

```properties
# Server
server.port=8081

# RabbitMQ Connection
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

# Consumer Settings
spring.rabbitmq.listener.simple.concurrency=2
spring.rabbitmq.listener.simple.max-concurrency=5
spring.rabbitmq.listener.simple.prefetch=10
spring.rabbitmq.listener.simple.default-requeue-rejected=false

# Actuator
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

---

## 🚀 Production Features

### Publisher Confirms & Returns

**What it does:**
- **Confirms:** RabbitMQ acknowledges message receipt with CorrelationData tracking
- **Returns:** Unroutable messages are detected and logged

**Example logs:**
```
✅ CONFIRM: Message successfully received by RabbitMQ broker
❌ REJECT: Message rejected by broker. Reason: channel error;
```

### Error Handling Strategy

```java
// Consumer Exception Handling
try {
    // Process message
} catch (IllegalArgumentException e) {
    // Validation error → DON'T retry
    throw new AmqpRejectAndDontRequeueException("Invalid payload", e);
} catch (Exception e) {
    // Infrastructure error → CAN retry
    throw e;
}
```

### Validation

**Producer Side:**
```java
@Valid OrderDTO order  // Validates before publishing
```

**Consumer Side:**
```java
messageValidator.validate(order);  // Re-validates after receiving
```

### Monitoring

**Health Endpoint:**
```bash
curl http://localhost:9081/actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "rabbit": {
      "status": "UP",
      "details": {
        "version": "4.1.0"
      }
    }
  }
}
```

---

## 🧪 Testing

### Manual Testing with cURL

See [Exchange Types](#-exchange-types) section for all cURL examples.

### Verify Message Flow

1. **Start Producer and Consumer**
2. **Send a test message**
3. **Check Producer logs:**
   ```
   ✅ Orders message sending to direct.exchange exchange, with orders.route key, with id: ef26796b-e7c7-443d-99a1-f6dd94526387
   ✅ CONFIRM: Message successfully received by RabbitMQ broker
   ```
4. **Check Consumer logs:**
   ```
   ✅ orders.queue message: product=Laptop, quantity=2
   ```

### Test Validation

**Send invalid order (quantity = 0):**
```bash
curl -X POST "http://localhost:8080/direct/order" \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "product": "Test", "quantity": 0}'
```

### RabbitMQ Management UI

1. Open http://localhost:15672
2. Login: `guest` / `guest`
3. View:
    - **Exchanges** → See all 4 exchanges
    - **Queues** → Monitor message rates
    - **Connections** → Active connections from Producer/Consumer

---

## 📁 Project Structure

```
Spring-Boot-RabbitMQ-Guide/
├── docker-compose.yml              # RabbitMQ container setup
├── pom.xml                        # Parent POM
│
├── rabbitmq-producer/             # Producer Microservice (Port 8080)
│   ├── src/main/java/com/example/
│   │   ├── configuration/
│   │   │   ├── DirectExchangeConfig.java
│   │   │   ├── FanoutExchangeConfig.java
│   │   │   ├── TopicExchangeConfig.java
│   │   │   ├── HeaderExchangeConfig.java
│   │   │   ├── RabbitConfiguration.java         # Publisher Confirms/Returns
│   │   │   └── RabbitInfrastructureInitializer.java
│   │   ├── controller/
│   │   │   ├── DirectController.java
│   │   │   ├── FanoutController.java
│   │   │   ├── TopicController.java
│   │   │   └── HeadersController.java
│   │   ├── dto/
│   │   │   ├── OrderDTO.java
│   │   │   ├── NotificationDTO.java
│   │   │   ├── UserEventDTO.java
│   │   │   ├── SystemEventDTO.java
│   │   │   └── PriorityMessageDTO.java
│   │   └── service/
│   │       └── RabbitMessagePublisher.java      # CorrelationData tracking
│   └── src/main/resources/
│       └── application.properties
│
└── rabbitmq-consumer/             # Consumer Microservice (Port 8081)
    ├── src/main/java/com/example/
    │   ├── configuration/
    │   │   ├── MessageValidator.java            # Jakarta Validation
    │   │   ├── RabbitConfiguration.java         # Consumer settings
    │   ├── dto/                                 # Same DTOs as Producer
    │   └── service/
    │       └── RabbitMessageListener.java       # @RabbitListener methods
    └── src/main/resources/
        └── application.properties
```

---

## 🎓 Key Concepts

### Publisher Confirms

Ensures messages are persisted by RabbitMQ broker:

```
✅ CONFIRM: Message successfully received by RabbitMQ broker
```

### Publisher Returns

Detects unroutable messages:

```
❌ REJECT: Message rejected by broker. Reason: channel error;
```

### CorrelationData

Tracks messages from publish to confirm:

```java
String messageId = UUID.randomUUID().toString();
CorrelationData correlationData = new CorrelationData(messageId);
rabbitTemplate.convertAndSend(exchange, key, message, correlationData);
```

### Wildcard Routing (Topic Exchange)

- `*` matches exactly one word: `user.signup.*` matches `user.signup.web`
- `#` matches zero or more words: `system.error.#` matches `system.error.critical.db`

### Headers Routing

Routes based on message headers, not routing key:

```java
message.getMessageProperties().setHeader("priority", "high");
```

---

## 📧 Contact

**Kemal** - sherefeddin1428@gmail.com

**Project Link:** https://github.com/KemalQ/Spring-Boot-RabbitMQ-Guide

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

Made with ❤️ using Spring Boot & RabbitMQ

</div>
