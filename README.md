![Website](https://img.shields.io/website?url=https%3A%2F%2Ffitness-tracker-backend-yrfj.onrender.com%2Fapi%2Fauth%2FisAlive&up_message=UP!&up_color=green&down_message=DOWN&down_color=red&style=flat&label=Service)
![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)
![Gradle](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?logo=jsonwebtokens&logoColor=white)
![Tests](https://img.shields.io/badge/Tests-73%20passed-brightgreen?logo=github-actions&logoColor=white)

# Gym Tracker

A REST API backend for tracking gym workouts. Users can log sessions, add exercises with detailed muscle-group information, record sets (weight / reps / RPE), build reusable workout plans, and browse a shared exercise catalog. The service is secured with JWT and supports role-based access control (`USER`, `MODERATOR`, `ADMIN`).

## Table of Contents

- [Tech Stack](#toc-tech-stack)
- [Features](#toc-features)
- [API Endpoints](#toc-api-endpoints)
  - [Authentication](#toc-authentication)
  - [Users](#toc-users)
  - [Workouts & Tracking](#toc-workouts-tracking)
  - [Exercises & Muscle Directory](#toc-exercises-muscle-directory)
  - [Workout Plans](#toc-workout-plans)
- [Try in Postman](#toc-postman)
- [Default Credentials](#toc-default-credentials)
- [Local Development](#toc-local-development)
- [Running Tests](#toc-running-tests)
- [Docker Deployment](#toc-docker-deployment)
  - [Prerequisites](#toc-prerequisites)
  - [Environment Variables](#toc-environment-variables)
  - [Quick Start (Docker CLI)](#toc-quick-start-cli)
  - [Quick Start (Docker Compose)](#toc-quick-start-compose)
  - [Notes](#toc-notes)

---

<a id="toc-tech-stack"></a>
## Tech Stack

| Layer | Technology |
| :--- | :--- |
| Language | Java 21 |
| Framework | Spring Boot 4.1 |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA / Hibernate |
| Database | PostgreSQL (prod) · H2 (tests) |
| Build | Gradle 9 (Kotlin DSL) |
| Containerization | Docker / Docker Compose |

---

<a id="toc-features"></a>
## Features

- User registration & login with JWT tokens
- Role-based access control (`USER`, `MODERATOR`, `ADMIN`)
- Start / complete gym sessions and track exercises set-by-set (weight, reps, RPE)
- Create reusable **workout plans** and apply them to a live session instantly
- Custom exercises with detailed multi-muscle mapping and anatomical categories
- Paginated workout history per user
- Soft-delete for user accounts
- Multi-language muscle names
- Auto-created admin account on first startup

---

<a id="toc-api-endpoints"></a>
## API Endpoints

<a id="toc-authentication"></a>
### Authentication

| Method | Path | Description | Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user account | Public |
| `POST` | `/api/auth/login` | Authenticate and receive a JWT access token | Public |
| `GET` | `/api/auth/whoami` | Get profile of the currently authenticated user | Required |
| `GET` | `/api/auth/isAlive` | Service health check | Public |

---

<a id="toc-users"></a>
### Users

| Method | Path | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/users/` | Paginated list of all users | Required |
| `POST` | `/api/users` | Create a new user | Required |
| `GET` | `/api/users/{uuid}` | Get user by UUID | Required |
| `PUT` | `/api/users/{uuid}` | Update user profile | Required |
| `DELETE` | `/api/users/{uuid}` | Soft-delete user account | Required |
| `GET` | `/api/users/check/username` | Check username availability (`?username=`) | Public |
| `GET` | `/api/users/check/email` | Check email availability (`?email=`) | Public |

---

<a id="toc-workouts-tracking"></a>
### Workouts & Tracking

| Method | Path | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/gym/workouts/my` | Paginated list of the current user's workouts | Required |
| `GET` | `/api/gym/user/workout/{uuid}` | Paginated workout history for a specific user | Required |
| `POST` | `/api/gym/workout/start` | Start a new workout session | Required |
| `POST` | `/api/gym/workout/{id}/complete` | Complete and finalize the active workout | Required |
| `GET` | `/api/gym/workout/{id}` | Get full workout details (exercises + sets) | Required |
| `POST` | `/api/gym/workout/{workoutId}/exercise/{exerciseId}` | Add an exercise to a workout | Required |
| `DELETE` | `/api/gym/workout/{wId}/exercise/{exId}` | Remove an exercise from a workout | Required |
| `POST` | `/api/gym/workout/exercise/{workoutExerciseId}/set` | Log a set (weight, reps, RPE) | Required |
| `DELETE` | `/api/gym/workout/exercise/{workoutExerciseId}/set/{setId}` | Delete a logged set | Required |
| `POST` | `/api/gym/workout/{wid}/plan/{pid}` | Populate a workout from a saved plan | Required |

---

<a id="toc-exercises-muscle-directory"></a>
### Exercises & Muscle Directory

| Method | Path | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/gym/exercise` | Paginated exercise catalog | Required |
| `POST` | `/api/gym/exercise` | Create a custom exercise with muscle mapping | Required |
| `GET` | `/api/gym/exercise/{id}` | Get exercise details by ID | Required |
| `DELETE` | `/api/gym/exercise/{id}` | Delete an exercise | Required |
| `GET` | `/api/gym/exercise/muscle` | Full muscle taxonomy with anatomical categories | Required |

---

<a id="toc-workout-plans"></a>
### Workout Plans

| Method | Path | Description | Auth |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/gym/workout/plan/{id}` | Get a workout plan by ID | Required |
| `POST` | `/api/gym/workout/plan` | Create a new workout plan | Required |
| `POST` | `/api/gym/workout/plan/{pid}/exercise/{eid}` | Add an exercise to a plan (`?orderNum=`) | Required |

---

<a id="toc-postman"></a>
## Try in Postman

[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/44209970-18191787-8fb8-4ba5-920a-2c30f07ed8c7?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D44209970-18191787-8fb8-4ba5-920a-2c30f07ed8c7%26entityType%3Dcollection%26workspaceId%3Ddb45adf4-03c5-4eff-94db-3fdd13e09359)

---

<a id="toc-default-credentials"></a>
## Default Credentials

An admin account is created automatically on first startup:

| Field | Value |
| :--- | :--- |
| Email | `admin@example.com` |
| Password | `admin` |
| Role | `ADMIN` |

---

<a id="toc-local-development"></a>
## Local Development

### H2 (no external database needed)

```bash
./gradlew bootRun --args='--spring.profiles.active=h2'
```

The H2 console is available at `http://localhost:8080/h2-console`.

### PostgreSQL (dev profile)

Set the required environment variables, then:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/gym_db
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=yourpassword
export SECRET_KEY=your-very-long-jwt-secret-key-at-least-256-bits
./gradlew bootRun
```

---

<a id="toc-running-tests"></a>
## Running Tests

Tests use an in-memory H2 database and require no external services:

```bash
./gradlew test
```

Test report is generated at `build/reports/tests/test/index.html`.

---

<a id="toc-docker-deployment"></a>
## Docker Deployment

<a id="toc-prerequisites"></a>
### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) & [Docker Compose](https://docs.docker.com/compose/install/)
- A running PostgreSQL instance (or use Docker Compose — it starts one automatically)

---

<a id="toc-environment-variables"></a>
### Environment Variables

| Variable | Description | Example |
| :--- | :--- | :--- |
| `DB_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://postgres:5432/gym_db` |
| `SPRING_DATASOURCE_USERNAME` | Database user | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `yourpassword` |
| `SECRET_KEY` | JWT signing secret (≥ 256-bit / 32 chars) | `change-me-to-a-long-random-string` |

---

<a id="toc-quick-start-cli"></a>
### Quick Start (Docker CLI)

1. **Build the image**
   ```bash
   docker build -t gym-tracker-api .
   ```
2. **Run**
   ```bash
   docker run -d \
     --name gym-tracker \
     -p 8080:8080 \
     -e DB_URL="jdbc:postgresql://host.docker.internal:5432/gym_db" \
     -e SPRING_DATASOURCE_USERNAME="postgres" \
     -e SPRING_DATASOURCE_PASSWORD="yourpassword" \
     -e SECRET_KEY="your-very-long-jwt-secret-key" \
     gym-tracker-api
   ```

---

<a id="toc-quick-start-compose"></a>
### Quick Start (Docker Compose)

No local PostgreSQL instance needed — `docker-compose.yml` spins up the database and the API together.

1. **Start everything**
   ```bash
   docker compose up --build
   ```
2. **Run in the background**
   ```bash
   docker compose up --build -d
   ```
3. **Stop**
   ```bash
   docker compose down
   ```
   Add `-v` to also wipe the database volume.

`docker-compose.yml`:
```yaml
services:
  postgres:
    image: 'postgres:latest'
    container_name: gym-tracker-postgres
    restart: unless-stopped
    environment:
      - POSTGRES_DB=gym_db
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=yourpassword
    ports:
      - '5432:5432'
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ['CMD-SHELL', 'pg_isready -U postgres -d gym_db']
      interval: 5s
      timeout: 5s
      retries: 10

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: gym-tracker-api
    restart: unless-stopped
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      - DB_URL=jdbc:postgresql://postgres:5432/gym_db
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=yourpassword
      - SECRET_KEY=your-very-long-jwt-secret-key
    ports:
      - '8080:8080'

volumes:
  postgres_data:
```

---

<a id="toc-notes"></a>
### Notes

- Change `POSTGRES_PASSWORD`, `SPRING_DATASOURCE_PASSWORD`, and `SECRET_KEY` before deploying anywhere outside local development.
- If port `5432` or `8080` is already in use, remap the left-hand side of the `ports` entry (e.g. `'15432:5432'`).
- Database data persists in the `postgres_data` volume between restarts — run `docker compose down -v` to wipe it.
