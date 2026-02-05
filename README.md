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

