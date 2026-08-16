# Gym tracker

- [Features](#toc-features)
- [API Endpoints](#toc-api-endpoints)
  - [Authentication](#toc-authentication)
  - [Users](#toc-users)
  - [Workouts & Tracking](#toc-workouts-tracking)
  - [Exercises & Muscle Directory](#toc-exercises-muscle-directory)
- [To try hit button below](#toc-postman)
- [Docker Deployment](#toc-docker-deployment)
  - [Prerequisites](#toc-prerequisites)
  - [Environment Variables](#toc-environment-variables)
  - [Quick Start (Docker CLI)](#toc-quick-start-cli)
  - [Quick Start (Docker Compose)](#toc-quick-start-compose)
  - [Notes](#toc-notes)

<a id="toc-features"></a>
## Features 
 - Login/register
 - JWT token authorization/authentication
 - Role based security
 - Creating own exercises with deep muscle information
 - Tracking gym sets
 - Multi-language support for muscles group
<a id="toc-api-endpoints"></a>
## API Endpoints
<a id="toc-authentication"></a>
### Authentication
- `POST /api/auth/register` — Register a new user account
- `POST /api/auth/login` — Authenticate user and receive access token
- `GET /api/auth/whoami` — Get profile details of the currently authenticated user
- `GET /api/auth/isAlive` — Service health check
---
<a id="toc-users"></a>
### Users
- `GET /api/users/` — Fetch paginated list of all users
- `POST /api/users` — Create a new user profile
- `GET /api/users/{uuid}` — Get user profile by UUID
- `PUT /api/users/{uuid}` — Update user profile information
- `DELETE /api/users/{uuid}` — Delete user account by UUID
- `GET /api/users/check/username` — Check if username is available
- `GET /api/users/check/email` — Check if email is available
---
<a id="toc-workouts-tracking"></a>
### Workouts & Tracking
- `POST /api/gym/workout/start` — Start a new active workout session
- `POST /api/gym/workout/stop/{id}` — Complete and finalize an active workout session
- `GET /api/gym/workout/{id}` — Get full workout details, including exercises and sets
- `GET /api/gym/user-workout/{uuid}` — Fetch paginated workout history for a specific user
- `POST /api/gym/workout/addExerciseToWorkout/{id}` — Add an exercise to an active workout
- `POST /api/gym/workout-exercise/{workoutExerciseId}/set` — Log a set (weight, reps, RPE) for a workout exercise
---
<a id="toc-exercises-muscle-directory"></a>
### Exercises & Muscle Directory
- `GET /api/gym/exercise` — Fetch paginated catalog of exercises
- `POST /api/gym/exercise` — Create a custom exercise with muscle impact mapping
- `GET /api/gym/exercise/{id}` — Get detailed exercise information by ID
- `DELETE /api/gym/exercise/{id}` — Remove an exercise from the catalog
- `GET /api/gym/exercise/muscle` — Retrieve full muscle taxonomy and anatomical categories
<a id="toc-postman"></a>
### To try hit button below
[<img src="https://run.pstmn.io/button.svg" alt="Run In Postman" style="width: 128px; height: 32px;">](https://app.getpostman.com/run-collection/44209970-62de4133-2f05-4a8f-8955-8d3d8ed32d30?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D44209970-62de4133-2f05-4a8f-8955-8d3d8ed32d30%26entityType%3Dcollection%26workspaceId%3Ddb45adf4-03c5-4eff-94db-3fdd13e09359)
<a id="toc-docker-deployment"></a>
## Docker Deployment
<a id="toc-prerequisites"></a>
### Prerequisites
* [Docker](https://docs.docker.com/get-docker/) & [Docker Compose](https://docs.docker.com/compose/install/)
* Running PostgreSQL database instance
---
<a id="toc-environment-variables"></a>
### Environment Variables
Before running the container, ensure the following environment variables are set:
| Variable | Description | Example |
| :--- | :--- | :--- |
| `DB_URL` | PostgreSQL connection string | `jdbc:postgresql://postgres:5432/gym_db` |
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
   -e DB_URL="jdbc:postgresql://host.docker.internal:5432/gym_db?user=postgres&password=yourpassword" \
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
      - DB_URL=jdbc:postgresql://postgres:5432/gym_db?user=postgres&password=yourpassword
    ports:
      - '8080:8080'

volumes:
  postgres_data:
```
---
<a id="toc-notes"></a>
### Notes
* Change `POSTGRES_PASSWORD` / the password in `DB_URL` before using anywhere outside local development.
* If port `5432` or `8080` is already taken on your machine, remap the left-hand side of the `ports` mapping (e.g. `'15432:5432'`).
* Database data persists in the `postgres_data` volume between restarts, unless you run `docker compose down -v`.
