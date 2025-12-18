# RememberBirthdays

> A fullstack Spring Boot application to help users remember and manage birthdays, featuring secure authentication, email reminders, and a modern frontend. Built to showcase strong backend engineering, security, and DevOps skills.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [API Overview](#api-overview)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Development](#local-development)
  - [Running Tests](#running-tests)
- [Project Structure](#project-structure)
- [Why This Project Stands Out](#why-this-project-stands-out)
- [Author](#author)

## Features

- **User Authentication & Authorization:**
  - Integrated with Keycloak for OAuth2-based authentication and role-based access control (Admin/User).
- **Birthday Management:**
  - Add, update, and delete birthdays for people you want to remember.
  - Each user manages their own list; admins can view all users and birthdays.
- **Automated Email Reminders:**
  - Sends daily email reminders for birthdays using Spring Boot Mail.
- **RESTful API:**
  - Well-structured endpoints for user and birthday management.
- **Frontend Ready:**
  - Designed to work with a React frontend (see `static/`).
- **Dockerized & Cloud-Ready:**
  - Includes Dockerfile and docker-compose for easy local development and deployment.
- **PostgreSQL Database:**
  - Uses PostgreSQL for robust, production-grade data storage.

## Architecture

- **Backend:** Java 21, Spring Boot 3.5, Spring Data JPA, Spring Security, Keycloak, PostgreSQL, Docker
- **DevOps:** Docker, Docker Compose

## API Overview

- `POST /api/user` — Register a new user
- `GET /api/user/{userId}` — Get user details
- `PUT /api/user/{userId}` — Update user profile
- `DELETE /api/user/{userId}` — Delete user
- `POST /api/people` — Add a birthday (authenticated)
- `GET /api/people` — List your birthdays
- `PUT /api/people/{id}` — Update a birthday
- `DELETE /api/people/{id}` — Delete a birthday
- `GET /api/admin/allUsers` — List all users (admin)
- `GET /api/admin/allBirthdays` — List all birthdays (admin)

## Getting Started

### Prerequisites

- Java 21+
- Docker & Docker Compose
- Maven

### Local Development

1. **Clone the repository:**
   ```sh
   git clone https://github.com/keshav1207/RememberBirthdays.git
   cd RememberBirthdays
   ```
2. **Configure environment variables:**
   - Copy `.env.example` to `.env` and fill in values for PostgreSQL and Keycloak.
   - **Email:**
     - Sign up for [SendGrid](https://sendgrid.com/), create an API key, and add these to your environment:
       - `SENDGRID_API_KEY=your_sendgrid_api_key`
       - `SENDGRID_FROM_EMAIL=your_verified_sender@email.com`
     - On Railway, add these in the environment variables section.
     - You must verify your sender email in SendGrid before sending.
3. **Start services:**
   ```sh
   docker-compose up --build
   ```
   - This will start PostgreSQL, Keycloak, and the backend API.
4. **Access Keycloak:**
   - Visit [http://localhost:8080](http://localhost:8080) to manage users/roles.
5. **API runs at:** [http://localhost:8081](http://localhost:8081)

### Running Tests

```sh
./mvnw test
```

## Environment Variables

```
# Example
DB_HOST=localhost
DB_PORT=5432
DB_USER=youruser
DB_PASS=yourpassword
DB_NAME=remember_birthdays

# SendGrid Email API
SENDGRID_API_KEY=your_sendgrid_api_key
SENDGRID_FROM_EMAIL=your_verified_sender@email.com
```

---

## Project Structure

- `src/main/java/com/example/RememberBirthdays/`
  - `Controller/` — REST API endpoints
  - `Service/` — Business logic, email, Keycloak integration
  - `Model/` — JPA entities (User, Person)
  - `Repository/` — Spring Data JPA repositories
  - `Config/` — Security and Keycloak configuration
  - `Utils/` — Security utilities
- `docker-compose.yml` — Local dev stack
- `Dockerfile` — Multi-stage backend build
- `pom.xml` — Maven dependencies

## Why This Project Stands Out

- **Modern Java & Spring Boot best practices**
- **Secure OAuth2 authentication with Keycloak**
- **Automated, real-world email reminders**
- **Clean, maintainable code with layered architecture**
- **Production-ready Docker setup**
- **Full CRUD and admin features**

## Author

- Keshav Callychurn [<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/linkedin/linkedin-original.svg" width="20" alt="LinkedIn"/>](https://www.linkedin.com/in/keshav0799)

---
