# Virtual Bank System with Microservices, BFF, and WSO2 API Gateway

A simplified virtual banking system built with Java Spring Boot microservices, a Backend for Frontend (BFF) pattern, Apache Kafka for event-driven logging, and secured via WSO2 API Manager.

---

## 🏗️ Architecture Overview

The system is organized into decoupled, independent services communicating over a shared Docker network:

* **API Gateway (WSO2):** Centralized entry point handling OAuth2 authentication, API keys, rate limiting, and request routing.
* **BFF Service (`bff-service`):** Aggregates data from multiple downstream microservices to optimize frontend payloads.
* **User Service (`user-service`):** Manages user credentials, registration, authentication, and profiles.
* **Account Service (`account-service`):** Manages bank accounts, balances, and executes scheduled jobs for stale accounts.
* **Transaction Service (`transaction-service`):** Handles deposits, withdrawals, fund transfers, and scheduled daily interest calculation.
* **Logging Service (`logging-service`):** Acts as a Kafka consumer to capture and store request/response audit logs.

---

## 🛠️ Technology Stack

* **Backend Language:** Java 11
* **Framework:** Spring Boot
* **Build Tool:** Maven
* **Containerization:** Docker & Docker Compose[cite: 1]
* **Messaging:** Apache Kafka & Zookeeper[cite: 1]
* **Database:** PostgreSQL / H2[cite: 1]
* **API Management:** WSO2 API Manager[cite: 1]

---

## 🚀 Getting Started

### Prerequisites
* [Docker Desktop](https://www.docker.com/) installed and running[cite: 1].
* Git installed[cite: 1].

### Clone the Repository
```bash
git clone [https://github.com/Mkhaleddd/Virtual-Bank-System-with-Microservices-BFF-WSO2-API-Gateway.git](https://github.com/Mkhaleddd/Virtual-Bank-System-with-Microservices-BFF-WSO2-API-Gateway.git)
cd Virtual-Bank-System-with-Microservices-BFF-WSO2-API-Gateway
