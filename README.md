# Agent Backend

Backend service for the **Career Agent Service**, built with Java 21 and Spring Boot 4.0.

This service acts as the central API Gateway and orchestrator. It handles user authentication, manages chat history via PostgreSQL, and coordinates communication between the Frontend (WebSocket/REST) and the AI Agent (RabbitMQ).

## 🏗 Architecture Overview

*   **API Gateway:** Exposes REST endpoints for the frontend (`agent_frontend`).
*   **WebSocket Server:** Manages real-time connections for chat streaming and notifications.
*   **Orchestrator:**
    *   Receives user messages and forwards them to the Python Agent (`career-agent`) via RabbitMQ (`prompt.queue`).
    *   Consumes agent responses from RabbitMQ (`response.queue`) and pushes them to the frontend via WebSocket.
    *   Manages database persistence for users, chats, messages, and vacancies.

## 🛠 Tech Stack

*   **Language:** Java 21
*   **Framework:** Spring Boot 4.0.1
*   **Database:** PostgreSQL (Relation data: Users, Chats, Vacancies)
*   **Messaging:** RabbitMQ (Spring AMQP)
*   **Security:** Spring Security + JWT (jjwt)
*   **Web:** Spring Web MVC, Spring WebSocket (STOMP)
*   **Data Access:** Spring Data JPA (Hibernate)
*   **Build Tool:** Maven

## ✨ Features

*   **Authentication & Authorization:** Secure signup/login with JWT Access & Refresh tokens.
*   **Chat Management:** CRUD operations for chat sessions and message history.
*   **Asynchronous Processing:** Decoupled AI processing using message queues.
*   **Real-time Communication:**
    *   `/topic/chat/{chatId}`: Streams AI text responses.
    *   `/topic/vacancies/{chatId}`: Pushes notifications about newly scraped jobs.
*   **Vacancy Management:** Stores and retrieves job listings scraped by the agent.

## 🚀 Getting Started

### Prerequisites

*   JDK 21
*   Maven (or use the included `mvnw` wrapper)
*   PostgreSQL running (default port 5432)
*   RabbitMQ running (default port 5672)

### Configuration

Configuration is managed in `src/main/resources/application.properties`.

Key environment variables/properties:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/agent_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672

# JWT Security
jwt.secret=your_base64_encoded_secret_key
jwt.access.expiration=900000
jwt.refresh.expiration=604800000
```

*Note: In the docker ecosystem, `spring.datasource.url` usually points to `jdbc:postgresql://db:5432/agent_db`.*

### Running the Application

1.  Navigate to the directory:
    ```bash
    cd agent_backend
    ```

2.  Build and run using the Maven wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```

The server will start on port **8080** by default.

## 📂 Project Structure

```
agent_backend/
├── src/main/java/com/example/demo/
│   ├── amq/                # RabbitMQ Producers & Consumers
│   ├── auth/               # Authentication Logic & Controllers
│   ├── chats/              # Chat Domain (Entities, Services, Controllers)
│   ├── infrastructure/     # Configs (Security, RabbitMQ, WebSocket)
│   ├── messages/           # Message Domain
│   ├── vacancies/          # Vacancy Domain
│   └── DemoApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── prompts/            # System prompts (if stored locally)
├── pom.xml                 # Maven dependencies
├── mvnw / mvnw.cmd         # Maven Wrapper
└── Dockerfile
```

## 🐋 Docker

A `Dockerfile` is included for containerization.

```bash
# Build
docker build -t agent-backend .

# Run
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=... agent-backend
```

In the full stack `docker-compose.yml`, this service depends on `db` (Postgres) and `rabbitmq`.
