# GiftKeeper Final Report

## Implemented application

GiftKeeper is a Java 17 desktop application for managing people, occasions, gift ideas, budgets, reminders and gift status transitions. The application is intentionally simple in its domain model so that the project can focus on automated software testing, build automation, continuous integration, code coverage, mutation testing and code quality.

## Architecture and design choices

The project is split into Maven modules: domain, persistence API, JPA persistence adapter, application services, GUI, CLI, BDD and E2E. The domain layer is independent from infrastructure. Repositories are represented as ports in the persistence API module. The JPA module implements those ports using Hibernate and PostgreSQL. The application module coordinates use cases and dependency injection with Google Guice. The GUI and CLI depend on the application layer.

## Testing strategy

The testing pyramid is respected by keeping most tests at domain and service level. Unit tests cover validation rules, status transitions, budgets, reminders and service behavior. Mockito is used where collaborators must be isolated. Integration tests exercise the JPA repositories against PostgreSQL started through Testcontainers. BDD tests describe user-level behavior with Cucumber. E2E tests execute a complete workflow through the application service and real persistence. GUI tests validate Swing behavior and interactions.

## Coverage and exclusions

JaCoCo and SonarCloud focus on executable business and persistence code. Bootstrap classes, module wiring, runners and GUI-heavy Swing infrastructure are excluded from coverage because they do not contain meaningful domain logic and are validated indirectly by integration, GUI, CLI, BDD and E2E tests. This follows the exam guideline that some code may be excluded from coverage when justified.

## Mutation testing

PIT is configured for the modules containing essential business logic: domain, application and JPA persistence. Mutation testing is intentionally focused on those modules to avoid excessive build time and to provide meaningful evidence on the correctness of the most important code.

## Docker and reproducibility

PostgreSQL is used as the real database. Integration and E2E tests start PostgreSQL automatically through Testcontainers during the Maven build, so the teacher only needs Java 17, Maven and Docker running locally. Local manual execution can also use the Docker Compose file in the docker folder.

## CI and code quality

GitHub Actions runs the Maven verification pipeline on Linux and performs SonarCloud analysis. The workflow uses `fetch-depth: 0` and runs GUI-related tests under Xvfb. SonarCloud receives JaCoCo XML reports generated during the build.

## How to reproduce

```bash
mvn clean verify
```

For mutation testing:

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

For the GUI:

```bash
docker compose -f docker/docker-compose.yml up -d
mvn -pl giftkeeper-gui -am exec:java -Dexec.mainClass=com.giftkeeper.gui.GiftKeeperGuiMain
```
