# GiftKeeper

## Overview

GiftKeeper is a modular Java application developed as part of the course:

**Automated Software Testing – 2024/2025**
Università degli Studi di Firenze

The project demonstrates advanced software testing techniques and modern software engineering practices, including:

- Test-Driven Development (TDD)
- Unit Testing
- Integration Testing
- End-to-End Testing
- Mutation Testing
- Continuous Integration
- Build Automation
- Code Quality Analysis
- Docker-based testing environments
- GitHub Actions CI pipelines

The application allows users to manage:

- People
- Gift ideas
- Occasions
- Gift statuses
- Reminder policies
- Gift history tracking

---

# Project Architecture

The project follows a modular Maven architecture:

```text
giftkeeper-parent
│
├── giftkeeper-domain
├── giftkeeper-app
├── giftkeeper-persistence-api
├── giftkeeper-persistence-jpa
├── giftkeeper-cli
├── giftkeeper-gui
├── giftkeeper-bdd
└── giftkeeper-e2e
```

---

# Modules Description

## giftkeeper-domain

Contains:

- Entities
- Value objects
- Enumerations
- Core business rules

Examples:

- Person
- Occasion
- GiftIdea
- GiftHistoryEntry
- ReminderPolicy

---

## giftkeeper-app

Application service layer.

Contains:

- Business use cases
- Dependency injection bootstrap
- Coordination logic between layers

Main service:

- `GiftKeeperUseCases`

---

## giftkeeper-persistence-api

Defines repository interfaces and persistence contracts.

Examples:

- PersonRepository
- OccasionRepository
- GiftIdeaRepository

---

## giftkeeper-persistence-jpa

Implements persistence using:

- Hibernate ORM
- PostgreSQL
- Jakarta Persistence API (JPA)

---

## giftkeeper-cli

Command-line interface demonstrating application usage.

---

## giftkeeper-gui

Desktop graphical interface implementation.

---

## giftkeeper-bdd

Behavior-driven testing module.

Contains higher-level behavioral scenarios.

---

## giftkeeper-e2e

End-to-end integration testing using:

- PostgreSQL Testcontainers
- Real database execution flow

---

# Technologies Used

| Technology           | Purpose                    |
| -------------------- | -------------------------- |
| Java 17              | Main programming language  |
| Maven                | Build automation           |
| JUnit 5              | Unit testing               |
| AssertJ              | Fluent assertions          |
| Mockito              | Mocking                    |
| Hibernate ORM        | Persistence                |
| PostgreSQL           | Database                   |
| Testcontainers       | Integration testing        |
| Docker               | Environment virtualization |
| GitHub Actions       | Continuous Integration     |
| JaCoCo               | Code coverage              |
| PIT Mutation Testing | Mutation analysis          |
| SonarCloud           | Code quality analysis      |

---

# Build Instructions

## Clean Build

```bash
mvn clean verify
```

---

## Run Mutation Testing

```bash
mvn org.pitest:pitest-maven:mutationCoverage
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

# Testing Strategy

The project applies multiple testing layers:

| Test Type         | Purpose                       |
| ----------------- | ----------------------------- |
| Unit Tests        | Validate isolated components  |
| Mock Tests        | Isolate dependencies          |
| Integration Tests | Verify module interaction     |
| End-to-End Tests  | Validate complete workflows   |
| Mutation Testing  | Evaluate test robustness      |
| BDD Tests         | Validate behavioral scenarios |

---

# Code Quality

The project integrates:

- SonarCloud analysis
- JaCoCo coverage reports
- PIT mutation testing
- GitHub Actions CI

The final version achieves:

- Clean CI pipeline
- No SonarCloud code smells
- High test coverage
- Modular architecture

---

# Docker Usage

The project uses PostgreSQL containers during integration and E2E tests through Testcontainers.

This guarantees:

- Reproducible environments
- Isolated database execution
- Reliable CI execution

---

# Continuous Integration

GitHub Actions automatically performs:

- Compilation
- Unit tests
- Integration tests
- Coverage analysis
- SonarCloud verification

---
