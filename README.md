
# 📓 Journal Management API

A production-ready **Spring Boot REST API** for a secure personal journal application. The API provides **JWT Authentication**, **Email OTP Verification**, **Journal Management**, **Mood & Date Filtering**, and **Motivational Quote Generation**.

---

## 🚀 Features

### Authentication & Security
- 🔐 JWT Authentication & Authorization
- 📧 Email OTP Verification
- 🔄 OTP Resend Support
- 🔒 BCrypt Password Encryption
- 🛡️ Spring Security

### Journal Management
- 📝 Create Journal Entries
- 📖 View All Journal Entries
- 🔍 View Journal Entry by ID
- ✏️ Update Journal Entries
- 🗑️ Delete Journal Entries
- 😊 Filter Entries by Mood
- 📅 Filter Entries by Date
- 💬 Generate Motivational Quotes

### Additional Features
- ✅ Request Validation
- ⚠️ Global Exception Handling
- 📚 Swagger/OpenAPI Documentation
- 🐳 Docker Support
- 🗄️ MySQL Database

---

## 🏗️ Architecture

```
React Frontend
       │
       │ REST API (JWT)
       ▼
Spring Boot Backend
       │
Spring Security
       │
Spring Data JPA
       │
      MySQL
```

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL 8
- JWT
- Maven
- Docker
- Docker Compose
- Swagger / OpenAPI
- Brevo SMTP

---

## 📂 Project Structure

```text
src
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── security
├── service
└── util
```

---

## ⚙️ Environment Variables

Create a `.env` file in the project root.

```env
DB_URL=

DB_USERNAME=

DB_PASSWORD=

JWT_SECRET=

JWT_EXPIRATION=86400000

MAIL_USERNAME=

MAIL_PASSWORD=

BREVO_API_KEY=

QUOTE_API_URL=https://dummyjson.com/quotes/random

SPRING_PROFILES_ACTIVE=prod

FRONTEND_URL=
```

An `.env.example` file is included in the repository as a reference.

---

## 📋 Prerequisites

Before running the application, ensure you have installed:

- Java 21
- Maven
- Docker & Docker Compose (Optional)
- MySQL 8 (If not using Docker)

---

## ▶️ Running Locally

Clone the repository

```bash
git clone https:https://github.com/Tanushri014/JournalEntry
```

Configure your `.env` file.

Build the project

```bash
mvn clean install
```

Run the application

```bash
mvn spring-boot:run
```

The server starts on

```
http://localhost:8081
```

---

## 🐳 Running with Docker

Build the Docker images

```bash
docker compose build
```

Start the containers

```bash
docker compose up -d
```

Stop the containers

```bash
docker compose down
```

---

## 📖 API Documentation

Swagger UI

```
http://localhost:8081/swagger-ui/index.html
```

OpenAPI JSON

```
http://localhost:8081/v3/api-docs
```

---

## 🔐 Authentication Flow

1. Register a new account.
2. Verify your email using the OTP.
3. Login with your credentials.
4. Copy the generated JWT token.
5. Click **Authorize** in Swagger.
6. Enter:

```
Bearer <your_token>
```

7. Access protected APIs.

---

## 📌 API Endpoints

### Authentication

| Method | Endpoint |
|--------|----------|
| POST | `/auth/register` |
| POST | `/auth/verify-otp` |
| POST | `/auth/resend-otp` |
| POST | `/auth/login` |


---

### User

| Method | Endpoint |
|--------|----------|
| GET | `/user` |
| PUT | `/user` |
| DELETE | `/user` |

---

### Journal

| Method | Endpoint |
|--------|----------|
| POST | `/journal` |
| GET | `/journal` |
| GET | `/journal/{id}` |
| PUT | `/journal/{id}` |
| DELETE | `/journal/{id}` |
| GET | `/journal/search?date=` |
| GET | `/journal/mood?mood=` |
| GET | `/journal/quote` |

---

## 🗄️ Database

- MySQL 8
- Spring Data JPA
- Hibernate ORM

Database schema is automatically managed using:

```properties
spring.jpa.hibernate.ddl-auto=update
```

## 👨‍💻 Author

**Tanushri Matre**

GitHub  
https://github.com/Tanushri014

LinkedIn  
https://www.linkedin.com/in/tanushri-matre-9756982a7

---

## ⭐ Support

If you found this project helpful, consider giving it a ⭐ on GitHub.






