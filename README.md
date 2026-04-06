# UAMS Backend - University Accommodation Management System

The **UAMS Backend** is a robust Spring Boot application that serves as the core engine for the University Accommodation Management System. It handles all business logic, data persistence, security, and complex report generation required by the university's accommodation office to manage student housing and billing.

## 🚀 Tech Stack

*   **Framework:** Spring Boot (Java)
*   **Database:** MySQL (Relational Database)
*   **Database Migrations:** Flyway (SQL Scripting)
*   **ORM:** Hibernate / Spring Data JPA
*   **Security:** Spring Security & JWT (JSON Web Tokens)
*   **API Rate Limiting:** Bucket4j
*   **Containerization:** Docker & Docker Compose

---

## 🏗️ Core Architecture & Modules

The backend architecture is highly modularised, separating responsibilities into domain-specific packages to maintain clean code and separation of concerns.

### 1. Domain Modules
The system is divided into clear business domains (`src/main/java/com/example/uams/module/`):
*   **Student:** Core student records, waiting lists, category management.
*   **Hall:** Residence halls, room capacities, and staff managers.
*   **Apartment:** Student apartments, bedrooms, and property inspections.
*   **Lease:** Lease agreements processing and validations.
*   **Invoice:** Billing tracks, rent generation, and overdue payment handling.
*   **Report:** Aggregates data to fulfill the 14 mandatory system reports.
*   **Course / Adviser:** Links students with their academic records.
*   **Staff:** Manages system users related to hall staffing operations.

### 2. Security Layer
*   **JWT Authentication:** Stateless authentication securing REST endpoints via the `JwtTokenProvider`.
*   **Spring Security:** Defines Role-Based Access Control paths protecting sensitive operations (`SecurityConfig.java`).

### 3. API Rate Limiting
*   Integrated **Bucket4j** through a custom `RateLimitFilter`. This ensures the API endpoints are protected against brute-force, DDoS attacks, or accidental high-volume spam by throttling requests.

---

## 📂 Project Structure

```text
├── src/main/java/com/example/uams/
│   ├── config/       # Spring configurations (Cors, Security)
│   ├── exception/    # Global exception handlers & custom errors
│   ├── module/       # Business modules (Advisers, Halls, Leases, Students, etc.)
│   ├── security/     # JWT filters, Rate limit filters, Authentication logic
│   └── seeder/       # Seeders to insert initial dummy data
├── src/main/resources/
│   ├── db/migration/ # Flyway SQL migration scripts (V1, V2...)
│   ├── application.properties # App configs
```

---

## ⚙️ Getting Started & Setup

### Prerequisites
*   Java Development Kit (JDK 17+)
*   Docker & Docker Compose (Optional for local DB, highly recommended)
*   Gradle

### 1. Setup Data Services
We recommend running MySQL within a Docker container. In the root of the project, run:
```bash
docker-compose up -d
```
*This will spin up the MySQL DB as defined in your `docker-compose.yml`.*

### 2. Database Migrations
Upon spring-boot startup, **Flyway** will automatically execute the files inside `src/main/resources/db/migration/` to build the required schema and insert initial constraints.

### 3. Running the Application
From the root directory, build and run the application via Gradle wrapper:
```bash
# Build the project
./gradlew build

# Run the app
./gradlew bootRun
```
The server will start at `http://localhost:8080`.

---

## 📚 API Endpoints Overview

While a full Swagger/Postman schema may be provided separately, here is a quick look at core API routes:

*   **`POST /api/auth/login`**: Authenticate and receive a JWT.
*   **`GET /api/reports/...`**: Access all 14 mandatory business reports (e.g. `/hall-managers`, `/unpaid-invoices`).
*   **`GET /api/students`**: Retrieve a list of students or query specific data.
*   **`POST /api/leases`**: Create a new lease agreement.

> ***Note:** Authentication (`Bearer Token`) is required on protected routes.*

---

## 🧪 Testing

The backend includes a comprehensive testing suite to verify authentication flow, rate limiting, and business logic without needing a running database instance (e.g., using Mockito).

To run the test suite:
```bash
./gradlew test
```
