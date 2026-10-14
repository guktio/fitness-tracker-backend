![Website](https://img.shields.io/website?url=https%3A%2F%2Ffitness-tracker-backend-yrfj.onrender.com%2Fapi%2Fauth%2FisAlive&up_message=UP!&up_color=green&down_message=DOWN&down_color=red&style=flat&label=Service)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)
![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?logo=jsonwebtokens&logoColor=white)
![OpenAPI](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?logo=swagger&logoColor=black)

# Gym Tracker API

A high-performance RESTful API backend for tracking workouts, managing custom exercises with anatomical muscle taxonomies, building reusable training plans, and logging live gym sessions in real time.

Powered by **Java 21**, **Spring Boot**, **PostgreSQL**, and **Redis** for distributed caching with fail-safe error handling.

---

## Table of Contents

- [Core Abilities & Features](#core-abilities--features)
- [Architecture & Tech Stack](#architecture--tech-stack)
- [Redis Caching Architecture](#redis-caching-architecture)
- [API Endpoints & Routes](#api-endpoints--routes)
  - [Authentication & Health](#authentication--health)
  - [Users Management](#users-management)
  - [Exercises & Muscle Catalog](#exercises--muscle-catalog)
  - [Workout Plans](#workout-plans)
  - [Live Workouts & Session Tracking](#live-workouts--session-tracking)
- [API Documentation (Swagger UI)](#api-documentation-swagger-ui)
- [Try in Postman](#try-in-postman)
- [Default Admin Credentials](#default-admin-credentials)
- [Local Development](#local-development)
  - [H2 In-Memory Profile](#h2-in-memory-profile)
  - [PostgreSQL & Redis Profile](#postgresql--redis-profile)
  - [Running Tests](#running-tests)
- [Docker Deployment](#docker-deployment)
  - [Environment Variables](#environment-variables)
  - [Docker Compose (Postgres + Redis + App)](#docker-compose-postgres--redis--app)

---

## Core Abilities & Features

- **Security & Authorization**:
  - Stateless authentication using **JWT** (HMAC-SHA256).
  - Role-Based Access Control (**RBAC**) with `USER`, `MODERATOR`, and `ADMIN` roles.
  - Automatic superuser bootstrapping on initial startup (`admin@example.com`).
  - Soft-delete support for user accounts preserving historical integrity.
- **Exercise Directory & Muscle Anatomy**:
  - Rich catalog of built-in compound and isolated movements seeded on boot.
  - Granular muscle engagement tracking classified into **Primary**, **Secondary**, and **Stabilizing** muscle groups.
  - Anatomical muscle categorization (Chest, Back, Arms, Legs, Core, etc.).
- **Reusable Workout Plans**:
  - Design multi-exercise templates with custom ordering.
  - Instant one-click instantiation of an entire workout session from a saved plan.
- **Live Workout Session Tracking**:
  - Active session state management (`IN_PROGRESS` $\rightarrow$ `COMPLETED`).
  - Granular set logging: records **Weight**, **Reps**, and **RPE** (Rate of Perceived Exertion).
  - Add, reorder, or delete exercises and sets dynamically during live training.
  - Paginated history and user-specific workout slices.
- **Distributed Redis Caching**:
  - Sub-millisecond response times on frequent reads (exercise details, plans, active sessions).
  - Isolated cache namespaces with dedicated serialization and TTLs.
  - Non-blocking failover: if Redis is unreachable, the system transparently falls back to PostgreSQL without failing requests.

---

## Architecture & Tech Stack

- **Runtime & Language**: Java 21 LTS
- **Framework**: Spring Boot 4.1 (`webmvc`, `security`, `data-jpa`, `cache`, `actuator`)
- **Primary Database**: PostgreSQL (Production / Dev) / H2 (Testing / In-memory dev)
- **Cache Engine**: Redis (via `spring-boot-starter-data-redis` & Spring Cache)
- **Object-Relational Mapping**: Hibernate ORM & JPA
- **Database Migrations & Auditing**: Flyway, Spring Data JPA Auditing
- **Documentation**: SpringDoc OpenAPI 3 / Swagger UI
- **Build Tool**: Gradle (Kotlin DSL)
- **Containerization**: Docker & Docker Compose

---

## Redis Caching Architecture

The application uses **Redis** as a distributed second-level cache managed through Spring Cache abstraction. Each cache namespace is tuned with a custom Time-To-Live (TTL) and typed JSON serialization:

| Cache Name | Entity / DTO Type | Default TTL | Description |
| :--- | :--- | :---: | :--- |
| `exercise` | `ExercisesDTO.Exercise` | 30 minutes | Exercise details and anatomical muscle impact |
| `plan` | `PlansDTO.Plan` | 30 minutes | Reusable workout plan templates |
| `workout` | `WorkoutsDTO.Workout` | 60 minutes | High-level workout session state (`IN_PROGRESS` / `COMPLETED`) |
| `workoutExercise` | `WorkoutsDTO.WorkoutExercise` | 30 minutes | Exercises added to an active workout |
| `workoutSet` | `SetsDTO.Set` | 30 minutes | Individual logged sets (weight, reps, RPE) |
| `workoutInfo` | `WorkoutsDTO.Info` | 30 minutes | Detailed composite workout view |

### Fail-Safe Cache Resilience
Implemented via `CacheConfig` with a custom `CacheErrorHandler`. If Redis goes offline or encounters a connection error, cache misses/errors are logged as warnings and queries proceed directly to PostgreSQL, ensuring **zero downtime** for client requests.

---

## API Endpoints & Routes

### Authentication & Health

Base path: `/api/auth`

| Method | Route | Description | Auth |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/auth/register` | Register a new user account | Public |
| `POST` | `/api/auth/login` | Authenticate with email/password and obtain JWT | Public |
| `GET` | `/api/auth/whoami` | Retrieve profile of the authenticated caller | Required |
| `GET` | `/api/auth/isAlive` | Health check endpoint | Public |

---

### Users Management

Base path: `/api/users`

| Method | Route | Description | Auth |
| :--- | :--- | :--- | :---: |
| `GET` | `/api/users/` | Paginated list of all users | `ADMIN` |
| `POST` | `/api/users` | Create user with explicit role assignment | `ADMIN` |
| `GET` | `/api/users/{uuid}` | Fetch user profile by UUID | Required |
| `PUT` | `/api/users/{uuid}` | Update user username or email | Owner / Admin |
| `DELETE` | `/api/users/{uuid}` | Soft-delete user account | Owner / Admin |
| `GET` | `/api/users/check/username` | Check username availability (`?username=`) | Public |
| `GET` | `/api/users/check/email` | Check email availability (`?email=`) | Public |

---

### Exercises & Muscle Catalog

Base path: `/api/gym`

| Method | Route | Description | Auth |
| :--- | :--- | :--- | :---: |
| `GET` | `/api/gym/exercise/{id}` | Get full exercise details by ID | Required |
| `POST` | `/api/gym/exercise` | Create a custom exercise with muscle impact | Required |
| `DELETE` | `/api/gym/exercise/{id}` | Delete an exercise | Required |
| `GET` | `/api/gym/exercise` | Paginated catalog (`?muscle=`, `?createdBy=`) | Required |
| `GET` | `/api/gym/exercise/muscle` | Grouped anatomical muscle taxonomy | Required |

---

### Workout Plans

Base path: `/api/gym`

| Method | Route | Description | Auth |
| :--- | :--- | :--- | :---: |
| `GET` | `/api/gym/workout/plan/{id}` | Retrieve workout plan by ID | Required |
| `POST` | `/api/gym/workout/plan` | Create a new workout plan template | Required |
| `POST` | `/api/gym/workout/plan/{pid}/exercise/{eid}` | Add an exercise to a plan (`?orderNum=`) | Required |

---

### Live Workouts & Session Tracking

Base path: `/api/gym`

| Method | Route | Description | Auth |
| :--- | :--- | :--- | :---: |
| `POST` | `/api/gym/workout/start` | Start a new live workout (`IN_PROGRESS`) | Required |
| `POST` | `/api/gym/workout/{id}/complete` | Mark active workout as `COMPLETED` | Required |
| `GET` | `/api/gym/workout/{id}` | Get complete workout info (exercises + sets) | Required |
| `POST` | `/api/gym/workout/{wid}/plan/{pid}` | Instantiate workout from a saved plan | Required |
| `POST` | `/api/gym/workout/{wId}/exercise/{exId}` | Add exercise to live workout session | Required |
| `DELETE` | `/api/gym/workout/{wId}/exercise/{exId}` | Remove exercise from workout session | Required |
| `POST` | `/api/gym/workout/exercise/{weId}/set` | Log a completed set (weight, reps, RPE) | Required |
| `DELETE` | `/api/gym/workout/exercise/{weId}/set/{sId}` | Remove a logged set | Required |
| `GET` | `/api/gym/workouts/my` | Paginated history of caller's workouts | Required |
| `GET` | `/api/gym/user/workout/{uuid}` | Paginated workout slice for a given user | Required |

---

## API Documentation (Swagger UI)

Interactive OpenAPI / Swagger documentation is available once the service is running:

- **Swagger UI**: [http://localhost:8080/docs](http://localhost:8080/docs)
- **OpenAPI JSON Spec**: [http://localhost:8080/api](http://localhost:8080/api)

---

## Try in Postman

Fork the official Postman collection to start testing all endpoints immediately:

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/44209970-18191787-8fb8-4ba5-920a-2c30f07ed8c7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D44209970-18191787-8fb8-4ba5-920a-2c30f07ed8c7%26entityType%3Dcollection%26workspaceId%3Ddb45adf4-03c5-4eff-94db-3fdd13e09359)

---

## Default Admin Credentials

A default superuser is provisioned automatically on first startup:

| Field | Value |
| :--- | :--- |
| Email | `admin@example.com` |
| Password | `admin` |
| Role | `ADMIN` |

---

## Local Development

### H2 In-Memory Profile
Runs completely standalone without external dependencies:

```bash
./gradlew bootRun --args='--spring.profiles.active=h2'
```
H2 Web Console: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### PostgreSQL & Redis Profile
Make sure PostgreSQL and Redis instances are running, export environment variables, and run:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/fitapp"
export SPRING_DATASOURCE_USERNAME="fitapp"
export SPRING_DATASOURCE_PASSWORD="yourpassword"
export REDIS_URL="redis://localhost:6379"
export SECRET_KEY="your-super-secret-key-that-must-be-at-least-32-bytes-long!"

./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Running Tests
Unit and integration tests run against an in-memory database:

```bash
./gradlew test
```
HTML test report is generated at `build/reports/tests/test/index.html`.

---

## Docker Deployment

### Environment Variables

| Variable | Description | Default / Example |
| :--- | :--- | :--- |
| `DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://postgres:5432/fitapp` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `fitapp` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `1234` |
| `REDIS_URL` | Redis connection URI | `redis://redis:6379` |
| `SECRET_KEY` | JWT signing secret key ($\ge$ 256 bits) | `your-super-secret-key-at-least-32-bytes!` |

### Docker Compose (Postgres + Redis + App)

The repository provides a complete `compose.yaml` orchestrating PostgreSQL, Redis, and the Spring Boot application:

```bash
# 1. Start all services in foreground
docker compose up --build

# 2. Start in background (detached)
docker compose up --build -d

# 3. Stop all services
docker compose down

# 4. Stop and remove volumes (clean wipe)
docker compose down -v
```

Services started by Docker Compose:
- **`app`**: Spring Boot API on port `8080`
- **`postgres`**: PostgreSQL database on port `5432` with healthcheck
- **`redis`**: Redis 8 cache on port `6379` with `redis-cli ping` healthcheck
