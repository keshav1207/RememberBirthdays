# RememberBirthdays – Backend

> Java 21 + Spring Boot 3.5 REST API for RememberBirthdays, with Keycloak authentication, automated birthday email reminders via SendGrid, and full AWS deployment managed with Terraform.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Features](#features)
4. [Architecture](#architecture)
5. [Project Structure](#project-structure)
6. [API Overview](#api-overview)
7. [Getting Started](#getting-started)
8. [CI/CD](#cicd)
9. [Infrastructure (Terraform)](#infrastructure-terraform)
10. [Monitoring](#monitoring)
11. [Author](#author)

---

## Project Overview

RememberBirthdays is a full-stack web application that helps users track and manage birthdays with automated email reminders. This repository contains the backend — a Spring Boot REST API secured with Keycloak (OAuth2 / JWT), backed by PostgreSQL, containerised with Docker, and deployed to AWS EC2 via ECR.

---

## Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security, OAuth2 Resource Server, Keycloak |
| Persistence | Spring Data JPA, PostgreSQL |
| Email | SendGrid Java SDK |
| Containerisation | Docker, Docker Compose |
| Registry | AWS ECR |
| Compute | AWS EC2 |
| Infrastructure | Terraform |
| Monitoring | Spring Actuator, Prometheus, Node Exporter |
| CI/CD | GitHub Actions |
| Testing | JUnit, Spring Boot Test, H2 (in-memory) |

---

## Features

- OAuth2 JWT authentication via Keycloak with role-based access (Admin / User)
- Full CRUD for birthday management — each user manages their own list
- Admin endpoints to view all users and birthdays across the system
- Automated daily email reminders using the SendGrid API
- Dockerised for consistent local development and production deployment
- Infrastructure fully provisioned with Terraform (networking, compute, database, CDN, security)
- Metrics exposed via Spring Actuator and scraped by Prometheus

---

## Architecture

![Architecture Diagram](images/Architectural%20Diagram%20-%20RememberBirthdays.png)

The backend runs as a Docker container on EC2, pulled from ECR on each deployment. PostgreSQL is provisioned via Terraform. CloudFront sits in front of the backend as a CDN. Keycloak runs separately on EC2 for authentication.

---

## Project Structure

```
src/main/java/com/example/RememberBirthdays/
├── Config/         # Security, OAuth2, and Keycloak configuration
├── Controller/     # REST API controllers
├── DTO/            # Data Transfer Objects
├── Model/          # JPA entities (User, Person)
├── Repository/     # Spring Data JPA repositories
├── Service/        # Business logic, email scheduling, Keycloak admin
└── Utils/          # Security utilities

terraform/
├── modules/
│   ├── cdn/        # CloudFront distribution
│   ├── compute/    # EC2 instances
│   ├── database/   # RDS / PostgreSQL
│   ├── networking/ # VPC, subnets, routing
│   ├── security/   # Security groups, IAM
│   └── storage/    # S3 buckets
├── main.tf
├── variables.tf
├── outputs.tf
└── providers.tf

.github/workflows/
├── backend-ci.yml  # Run tests on push / PR
└── backend-cd.yml  # Build, push to ECR, deploy to EC2
```

---

## API Overview

### User

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/user` | Register a new user | Public |
| `GET` | `/api/user/{userId}` | Get user details | Authenticated |
| `PUT` | `/api/user/{userId}` | Update user profile | Authenticated |
| `DELETE` | `/api/user/{userId}` | Delete user | Authenticated |

### Birthdays

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/people` | Add a birthday | Authenticated |
| `GET` | `/api/people` | List your birthdays | Authenticated |
| `PUT` | `/api/people/{id}` | Update a birthday | Authenticated |
| `DELETE` | `/api/people/{id}` | Delete a birthday | Authenticated |

### Admin

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/api/admin/allUsers` | List all users | Admin |
| `GET` | `/api/admin/allBirthdays` | List all birthdays | Admin |

---

## Getting Started

### Prerequisites

- Java 21+
- Docker & Docker Compose
- Maven

### Local Development

1. Clone the repository:

```bash
git clone https://github.com/keshav1207/RememberBirthdays.git
cd RememberBirthdays
```

2. Configure environment variables — copy `.env.example` to `.env` and fill in values for PostgreSQL, Keycloak, and SendGrid:

```
SENDGRID_API_KEY=your_sendgrid_api_key
SENDGRID_FROM_EMAIL=your_verified_sender@email.com
```

> You must verify your sender email in SendGrid before it will send.

3. Start all services (PostgreSQL, Keycloak, backend):

```bash
docker compose -f docker-compose-local.yml up --build
```

4. The API runs at `http://localhost:8081` and Keycloak at `http://localhost:8080`.

### Running Tests

```bash
./mvnw test
```

Tests use an H2 in-memory database — no external services required.

---

## CI/CD

Two GitHub Actions workflows handle automated testing and deployment.

**CI** (`backend-ci.yml`) — runs on every push and pull request to `main`:
- Sets up Java 21 (Temurin)
- Runs the full test suite with `./mvnw test`

**CD** (`backend-cd.yml`) — runs on push to `main` only:
- Runs tests (must pass before deploy)
- Builds and pushes a Docker image to AWS ECR
- SSHs into the EC2 instance, pulls the new image, and restarts the stack with `docker compose up -d`

Required GitHub secrets: `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `AWS_REGION`, `ECR_REGISTRY`, `ECR_REPOSITORY`, `EC2_HOST`, `EC2_USERNAME`, `EC2_SSH_KEY`.

---

## Infrastructure (Terraform)

All AWS infrastructure is defined in the `terraform/` directory and organised into modules:

- **networking** — VPC, subnets, internet gateway, route tables
- **security** — security groups, IAM roles and policies
- **compute** — EC2 instances for the backend and Keycloak
- **database** — PostgreSQL
- **cdn** — CloudFront distribution in front of the backend
- **storage** — S3 buckets

To provision from scratch:

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

---

## Monitoring

The backend exposes metrics via Spring Actuator at `/actuator/prometheus`. Prometheus scrapes these every 15 seconds alongside Node Exporter host metrics, giving visibility into both application-level and system-level health.

---

## Author

Keshav Callychurn [<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/linkedin/linkedin-original.svg" width="20" alt="LinkedIn"/>](https://www.linkedin.com/in/keshav0799)
