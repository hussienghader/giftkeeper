# GiftKeeper – Automated Software Testing Project

[![CI](https://github.com/hussienghader/giftkeeper/actions/workflows/ci.yml/badge.svg)](https://github.com/hussienghader/giftkeeper/actions/workflows/ci.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=hussienghader_giftkeeper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hussienghader_giftkeeper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=hussienghader_giftkeeper&metric=coverage)](https://sonarcloud.io/summary/new_code?id=hussienghader_giftkeeper)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=hussienghader_giftkeeper&metric=bugs)](https://sonarcloud.io/summary/new_code?id=hussienghader_giftkeeper)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=hussienghader_giftkeeper&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=hussienghader_giftkeeper)

GiftKeeper is a Java 17 desktop application developed for the Automated Software Testing course. The goal is intentionally simple: managing people, occasions, gift ideas, budgets, reminders and gift status transitions. The academic focus is the complete testing and delivery pipeline: TDD-oriented design, unit tests, mocking, integration tests, UI tests, end-to-end tests, Docker/Testcontainers, Maven build automation, GitHub Actions, mutation testing and SonarCloud quality analysis.

## Architecture

```text
giftkeeper/
├── giftkeeper-domain              # Pure domain model and business rules
├── giftkeeper-persistence-api     # Repository interfaces / ports
├── giftkeeper-persistence-jpa     # JPA/Hibernate PostgreSQL adapter
├── giftkeeper-app                 # Application services and Google Guice modules
├── giftkeeper-gui                 # Swing desktop GUI
├── giftkeeper-cli                 # Command-line interface
├── giftkeeper-bdd                 # Cucumber BDD scenarios
├── giftkeeper-e2e                 # End-to-end tests with PostgreSQL/Testcontainers
├── docker                         # Local PostgreSQL Docker Compose setup
├── docs                           # Report and setup notes
└── .github/workflows              # CI workflow
```

The design separates domain, application services, persistence adapters and user interfaces. The GUI and CLI depend on the application layer; the domain does not depend on Swing, Maven, JPA, Docker or any infrastructure framework.

## Course topic mapping

| Course topic | GiftKeeper evidence |
|---|---|
| Java | Java 17 LTS configured in Maven and Eclipse metadata |
| JUnit / Unit Testing | JUnit 5 tests in domain, application, persistence, GUI and CLI modules |
| TDD | Domain and service behavior is expressed as readable executable specifications |
| Mocking | Mockito isolates service, GUI and CLI collaborators |
| Maven | Multi-module Maven build; one-command verification with `mvn clean verify` |
| Code Coverage | JaCoCo XML/HTML reports; SonarCloud imports the reports |
| Mutation Testing | PIT configured for domain, application and JPA business logic |
| Docker | PostgreSQL is started automatically by Testcontainers during integration/E2E tests |
| Integration Tests | JPA repositories tested against a real PostgreSQL container |
| UI Tests | Swing GUI tested through JUnit/Mockito/AssertJ assertions under Xvfb in CI |
| End-to-End Tests | Complete application workflow executed with PostgreSQL/Testcontainers |
| BDD | Cucumber feature and step definitions |
| Git / GitHub | Public repository, meaningful commits, GitHub Actions workflow |
| Code Quality | SonarCloud quality gate, coverage and issue checks |
| High-rating elements | SQL database with JPA/Hibernate, Google Guice, GUI + CLI, Cucumber BDD |

## Requirements

- Java 17
- Maven 3.9+
- Docker Desktop or Docker Engine
- Git

Docker must be running before executing the full verification because integration and E2E tests use Testcontainers.

## Full verification

From the repository root:

```bash
mvn clean verify
```

Expected result:

```text
BUILD SUCCESS
Failures: 0, Errors: 0, Skipped: 0
```

## Mutation testing

Run PIT on the modules containing business logic and persistence logic:

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

Generated reports:

```text
giftkeeper-domain/target/pit-reports/index.html
giftkeeper-app/target/pit-reports/index.html
giftkeeper-persistence-jpa/target/pit-reports/index.html
```

The final evidence should show no survived mutants for the selected essential business-logic modules.

## Run the GUI

Start a local PostgreSQL database:

```bash
docker compose -f docker/docker-compose.yml up -d
```

Run the Swing GUI:

```bash
mvn -pl giftkeeper-gui -am exec:java -Dexec.mainClass=com.giftkeeper.gui.GiftKeeperGuiMain
```

## Run the CLI

```bash
mvn -pl giftkeeper-cli -am exec:java -Dexec.mainClass=com.giftkeeper.cli.GiftKeeperCliMain
```

## SonarCloud

Automatic analysis should be disabled in SonarCloud. The project is analyzed from GitHub Actions after Maven has generated JaCoCo reports.

Required GitHub secret:

```text
SONAR_TOKEN
```

Manual analysis example:

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=hussienghader_giftkeeper \
  -Dsonar.organization=hussienghader \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=YOUR_SONAR_TOKEN
```

## Eclipse import

The repository contains Eclipse metadata (`.project`, `.classpath`, `.settings`) for the parent project and all Maven modules. For a final check, clone the repository into a fresh folder and import it in Eclipse with Java 17 configured. All modules should import without errors or warnings, and tests should be runnable as JUnit tests.

## Repository cleanliness

Generated files must not be committed. In particular, keep these out of Git:

```text
target/
*.class
.idea/
.vscode/
*.log
.env
```

Eclipse metadata is intentionally included because the exam guidelines require the project to be importable as Eclipse project(s).

## Final submission checklist

Before sending the repository to the teacher, verify:

- `mvn clean verify` passes locally.
- Docker/Testcontainers tests run with `Skipped: 0`.
- PIT mutation reports are generated and show no survived mutants on selected modules.
- GitHub Actions is green.
- SonarCloud Quality Gate is passed.
- SonarCloud coverage is 100% after justified exclusions.
- SonarCloud issues are 0.
- The GUI opens with the command above.
- The project imports in a fresh Eclipse workspace with Java 17 and no errors/warnings.
- The final report is included as Markdown or PDF, not Word/LibreOffice.
