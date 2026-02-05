# URL Shortener Service (Spring Boot)

A backend URL shortening service built with Spring Boot.  
It provides URL creation, redirection, caching, expiration handling, and IP-based rate limiting using Redis.

---

## Features

- Generate short URLs using Base62 hashing
- Persistent storage with MySQL (JPA/Hibernate)
- Redis caching for fast URL resolution
- Token bucket rate limiting (IP-based)
- URL expiration scheduler
- Collision detection and retry logic
- Input URL validation
- Global exception handling
- Global CORS configuration
- Structured layered architecture

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Redis
- Docker & Docker Compose
- AspectJ (logging)
- Maven

---

## Architecture Overview

The application follows a layered architecture:

Controller → Service → Repository → Database  
                     ↓  
                 Redis Cache

Core modules:

- Controller layer → REST endpoints
- Service layer → business logic
- Repository layer → persistence
- Cache layer → Redis URL caching
- Rate limiter → Redis token bucket algorithm
- Scheduler → expiration cleanup
- Validation → URL normalization and checks

---

## API Endpoints

### Create short URL

POST /api/shorten

Request body:

```json
{
  "url": "https://example.com"
}
```

Response:

```json
{
  "shortUrl": "http://localhost:8083/api/abc1234"
}
```

---

### Redirect to original URL

GET /api/{code}

Returns HTTP 302 redirect.

---

## Rate Limiting

Token bucket algorithm implemented in Redis.

- Shorten endpoint: stricter limits
- Redirect endpoint: higher throughput
- Preflight OPTIONS requests excluded from limiting

---

## CORS

Global CORS configuration is enabled.

- Allows cross-origin frontend access
- OPTIONS preflight requests supported
- Compatible with browser clients

---

## Running with Docker

Build and start all services:

```bash
docker compose up --build
```

Services started:

- MySQL (database)
- Redis (cache + rate limiting)
- Spring Boot app

Application runs at:
http://localhost:8083



---

## Future Improvements

- Custom domain support
- Analytics and click tracking
- Admin dashboard
- Distributed deployment scaling
- Authentication layer
