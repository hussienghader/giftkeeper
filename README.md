# GiftKeeper

## Overview

GiftKeeper is a modular Java application designed to manage people, occasions, gift ideas, budgets, and gift histories.

The project was developed as part of the _Automated Software Testing_ course and focuses on advanced software engineering practices including:

- Test-Driven Development (TDD)
- Unit Testing
- Integration Testing
- End-to-End Testing
- Mutation Testing
- Continuous Integration
- Code Quality Analysis
- Dockerized Testing Environments

The application provides both:

- Command Line Interface (CLI)
- Desktop Graphical User Interface (Swing GUI)

The architecture separates domain logic, application services, persistence contracts, and persistence implementations into independent Maven modules.

---

# Main Technologies

| Technology     | Purpose                       |
| -------------- | ----------------------------- |
| Java 17        | Main programming language     |
| Maven          | Multi-module build automation |
| JUnit 5        | Unit and integration testing  |
| AssertJ        | Fluent assertions             |
| Mockito        | Mocking dependencies          |
| Hibernate/JPA  | ORM and persistence           |
| PostgreSQL     | Relational database           |
| Testcontainers | Dockerized testing            |
| Docker         | Reproducible environments     |
| GitHub Actions | Continuous Integration        |
| JaCoCo         | Code coverage                 |
| PIT            | Mutation testing              |
| SonarCloud     | Static code analysis          |

---

# Project Structure

```text
giftkeeper/
│
├── giftkeeper-domain
├── giftkeeper-app
├── giftkeeper-persistence-api
├── giftkeeper-persistence-jpa
├── giftkeeper-cli
├── giftkeeper-gui
├── giftkeeper-e2e
├── giftkeeper-bdd
│
├── docker
├── .github/workflows
│
├── pom.xml
└── README.md
```

---

# Architecture

The project follows a layered and modular architecture.

## Domain Layer

Contains:

- business entities
- domain validation
- domain rules
- immutable identifiers
- business state transitions

Examples:

- Person
- Occasion
- GiftIdea
- Budget
- GiftHistoryEntry

---

## Application Layer

Contains use cases and orchestration logic.

The application layer coordinates repositories and domain entities without depending on infrastructure details.

Examples:

- createPerson
- createOccasion
- createGiftIdea
- changeGiftStatus

---

## Persistence API Layer

Defines repository interfaces used by the application layer.

This layer decouples business logic from persistence implementation.

Examples:

- PersonRepository
- OccasionRepository
- GiftIdeaRepository

---

## Persistence JPA Layer

Implements repositories using:

- Hibernate
- JPA
- PostgreSQL

This layer is used in integration and end-to-end tests.

---

## User Interfaces

The application provides:

- CLI interface
- Swing GUI interface

Both interfaces use the same application services.

---

# Testing Strategy

The project applies multiple levels of automated testing.

## Unit Tests

Unit tests isolate components and verify:

- domain validation
- business rules
- edge cases
- state transitions

Mockito is used when dependency isolation is required.

InMemory repositories are intentionally used only in unit tests to support fast TDD cycles and isolated component testing.

---

## Integration Tests

Integration tests verify collaboration between:

- application services
- JPA repositories
- Hibernate
- PostgreSQL

These tests use:

- Docker
- Testcontainers

A real PostgreSQL container is automatically started during test execution.

---

## End-to-End Tests

End-to-end tests validate complete application flows through the real persistence layer.

The tests verify:

- database interaction
- application orchestration
- full workflow execution

---

## Mutation Testing

PIT mutation testing is used to evaluate the effectiveness of the test suite.

The goal is not only high coverage percentages, but also strong fault detection capability.

---

# Continuous Integration

GitHub Actions automatically executes:

- Maven build
- unit tests
- integration tests
- mutation testing
- code quality analysis

on every push.

---

# Code Quality

The project integrates SonarCloud for:

- static analysis
- maintainability checks
- code smell detection
- quality gate verification

---

# Running the Project

## Requirements

- Java 17
- Maven 3.9+
- Docker Desktop

---

## Build

```bash
mvn clean verify
```

---

## Run CLI

```bash
mvn -pl giftkeeper-cli exec:java
```

---

## Run GUI

```bash
mvn -pl giftkeeper-gui exec:java
```

---

# Running Tests

## Unit + Integration Tests

```bash
mvn test
```

## Full Verification

```bash
mvn verify
```

## Mutation Testing

```bash
mvn -Pmutation test
```

---

# Educational Goals

The project was developed to demonstrate practical application of:

- automated software testing
- modular architecture
- build automation
- continuous integration
- containerized testing
- maintainable software design

within a realistic Java application.
