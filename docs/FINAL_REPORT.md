# GiftKeeper Final Academic Report

## 1. Project overview

GiftKeeper is a Java 17 application for managing people, occasions, gift ideas, budgets, reminders and gift purchase status. The project was designed for an academic course on Test-Driven Development, Build Automation and Continuous Integration. The goal is not only to deliver a working application, but to demonstrate a complete software testing and quality workflow.

## 2. Architecture

The project follows a clean modular architecture:

- `giftkeeper-domain`: pure domain model and business rules.
- `giftkeeper-persistence-api`: repository interfaces acting as ports.
- `giftkeeper-persistence-jpa`: PostgreSQL JPA/Hibernate implementation.
- `giftkeeper-app`: application services and dependency injection.
- `giftkeeper-gui`: Swing graphical interface.
- `giftkeeper-cli`: command-line interface.
- `giftkeeper-bdd`: Cucumber behavior-driven scenarios.
- `giftkeeper-e2e`: end-to-end workflow tests.

This structure keeps business logic independent from persistence and UI details, making the project easier to test and maintain.

## 3. Database

The project uses a real PostgreSQL database through JPA/Hibernate. The database is available in two ways:

1. Local manual execution through Docker Compose.
2. Automated integration/E2E tests through Testcontainers.

This avoids relying only on fake persistence and demonstrates database integration testing with a real DBMS.

## 4. Testing strategy

### Unit testing

Domain objects and application services are tested with JUnit 5 and AssertJ. The tests validate business rules such as invalid names, birth dates, gift prices, reminder policies, budget checks and gift status changes.

### Mocking

Mockito is used to isolate application services from collaborators. This demonstrates testing behavior without depending on real external services.

### Integration testing

JPA repositories are tested against PostgreSQL using Testcontainers. These tests verify entity mapping, transactions and repository behavior using a real database. In the corrected version, these tests are not silently skipped: Docker/Testcontainers problems are visible in the build so the submitted evidence is trustworthy.

### UI testing

The Swing GUI is tested with UI-oriented tests to verify that the frame can interact with the application service and display data correctly.

### BDD

Cucumber is used to express user-visible behavior in a high-level feature file and connect it to Java step definitions.

### E2E testing

The E2E module verifies a complete application workflow: creating a person, occasion and gift, changing gift status and reading the final state.

## 5. Build automation

Maven is used as the build automation tool. The full project is verified with:

```bash
mvn clean verify   # must end with BUILD SUCCESS and Skipped: 0
```

The project is multi-module, so Maven builds the modules in the correct dependency order.

## 6. Code coverage

JaCoCo generates HTML and XML reports. These reports provide evidence that the automated tests cover the main production code.

## 7. Mutation testing

PIT is configured to assess the strength of the tests. Mutation testing is stronger than line coverage because it checks whether tests detect small changes in production code.

Recommended command:

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

## 8. Continuous Integration

GitHub Actions runs the automated verification pipeline on every push and pull request. The workflow builds the project, runs tests, generates reports, runs mutation testing evidence and integrates with SonarCloud when secrets are configured.

## 9. Code quality

SonarCloud is used for static analysis, code smells, maintainability and quality-gate evidence. The project includes a SonarCloud-ready configuration and workflow.

## 10. Docker

Docker is used for PostgreSQL, both for local development and for reproducible integration testing. This avoids installing PostgreSQL manually on each machine and supports reproducible testing. The project includes Windows helper scripts and classpath Testcontainers configuration to avoid stale user-level Docker settings.

## 11. Final verification commands

```bash
mvn clean verify   # must end with BUILD SUCCESS and Skipped: 0
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

## 12. Conclusion

GiftKeeper demonstrates the main topics expected in the course: TDD, JUnit, Maven build automation, Mockito, JaCoCo, PIT, Docker, Testcontainers, BDD, E2E testing, GUI testing, GitHub Actions and SonarCloud. The project is structured to be understandable in an oral exam and reproducible on another machine.
