# GiftKeeper – Final Academic Submission

[![Build](https://github.com/alhusseinghader3/giftkeeper/actions/workflows/ci.yml/badge.svg)](https://github.com/alhusseinghader3/giftkeeper/actions/workflows/ci.yml)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=alhusseinghader3_giftkeeper&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=alhusseinghader3_giftkeeper)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=alhusseinghader3_giftkeeper&metric=coverage)](https://sonarcloud.io/summary/new_code?id=alhusseinghader3_giftkeeper)

GiftKeeper is a Java 17 multi-module Maven project prepared for an academic course on **Test-Driven Development, Build Automation, Continuous Integration, Docker, Testing and Code Quality**. The project is intentionally structured to demonstrate clean architecture, automated tests at several levels, reproducible builds, CI, database integration, mutation testing and SonarCloud readiness.

## 1. Main features

GiftKeeper helps manage people, occasions, gift ideas, budgets, reminders and gift status transitions. The application has both a Swing GUI and a CLI entry point, while the business logic is kept independent from the user interface and persistence technology.

## 2. Architecture

```text
giftkeeper/
├── giftkeeper-domain              # Pure domain model and business rules
├── giftkeeper-persistence-api     # Repository interfaces / ports
├── giftkeeper-persistence-jpa     # JPA/Hibernate PostgreSQL adapters
├── giftkeeper-app                 # Application services and dependency injection
├── giftkeeper-gui                 # Swing GUI
├── giftkeeper-cli                 # Command-line entry point
├── giftkeeper-bdd                 # Cucumber BDD scenarios
├── giftkeeper-e2e                 # End-to-end tests
├── docker                         # PostgreSQL docker-compose and schema
├── docs                           # Academic report and setup documentation
└── .github/workflows              # GitHub Actions CI workflow
```

The design follows a layered/hexagonal style: domain code is independent, repositories are ports, JPA is an adapter, and GUI/CLI depend on the application layer rather than on the database directly.

## 3. Database usage

Yes, the project uses a real database.

- DBMS: PostgreSQL
- Persistence framework: JPA/Hibernate
- Local DB execution: Docker Compose
- Test DB execution: Testcontainers

Local PostgreSQL can be started with:

```bash
docker compose -f docker/docker-compose.yml up -d
```

Default local configuration:

```text
JDBC URL: jdbc:postgresql://localhost:5432/giftkeeper
User:     giftkeeper
Password: giftkeeper
```

## 4. Academic mapping

| Course topic | Evidence in GiftKeeper |
|---|---|
| TDD | Domain/application behavior is specified through tests before/alongside implementation |
| JUnit | JUnit 5 tests in domain, app, persistence, GUI, CLI, BDD and E2E modules |
| Build automation | Maven multi-module build with `clean verify` |
| Maven modules | Separate modules for domain, API, JPA, app, GUI, CLI, BDD and E2E |
| Mocking | Mockito tests for application collaborators and UI/CLI boundaries |
| Code coverage | JaCoCo HTML/XML reports per relevant module |
| Mutation testing | PIT configured for domain, app and persistence code |
| Database integration tests | PostgreSQL with Testcontainers in the JPA module |
| UI tests | Swing GUI tests using AssertJ Swing/JUnit |
| BDD | Cucumber feature file and Java step definitions |
| E2E | Complete workflow through the application service with real PostgreSQL |
| Docker | PostgreSQL service in `docker/docker-compose.yml` |
| CI | GitHub Actions workflow in `.github/workflows/ci.yml` |
| Code quality | SonarCloud-ready configuration and CI artifacts |

## 5. Requirements

- Java 17
- Maven 3.9+
- Docker Desktop for Testcontainers/integration evidence
- Git for GitHub submission

## 6. Full local verification

From the root folder:

```bash
mvn clean verify
```

or on Linux/macOS:

```bash
./mvnw clean verify
```

On Windows PowerShell:

```powershell
.\mvnw.cmd clean verify
```

Expected final result:

```text
BUILD SUCCESS
Tests run: ..., Failures: 0, Errors: 0, Skipped: 0
```

For final academic evidence, keep Docker Desktop open before running the command. In this corrected version, PostgreSQL integration and E2E tests are strict: if Docker/Testcontainers is unavailable, verification fails instead of silently hiding the missing integration evidence.

## 7. Windows Testcontainers fix

If Docker works with `docker run hello-world` but Maven/Testcontainers still fails, run:

```powershell
.\scripts\fix-testcontainers-windows.ps1
```

This removes old user-level Testcontainers settings and switches Docker to the Docker Desktop Linux engine. The project also contains classpath-level `testcontainers.properties` and `docker-java.properties` files so the build does not depend on stale settings in `C:\Users\<you>\.testcontainers.properties`.

## 8. Mutation testing

Use this command because it includes all required dependent modules:

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

Generated PIT reports are under:

```text
*/target/pit-reports/index.html
```

## 9. Coverage reports

After `mvn clean verify`, JaCoCo reports are generated under:

```text
*/target/site/jacoco/index.html
*/target/site/jacoco/jacoco.xml
```

## 10. Run the GUI

Start the database:

```bash
docker compose -f docker/docker-compose.yml up -d
```

Run the Swing GUI:

```bash
mvn -pl giftkeeper-gui -am exec:java -Dexec.mainClass=com.giftkeeper.gui.GiftKeeperGuiMain
```

## 11. Run the CLI

```bash
mvn -pl giftkeeper-cli -am exec:java -Dexec.mainClass=com.giftkeeper.cli.GiftKeeperCliMain
```

## 12. GitHub and SonarCloud

1. Create a GitHub repository named `giftkeeper`.
2. Push this project.
3. Create a SonarCloud project.
4. Add GitHub secret: `SONAR_TOKEN`.
5. Add GitHub variable: `SONAR_ORGANIZATION`.
6. The workflow will run build, tests, PIT evidence and SonarCloud analysis.

Manual SonarCloud command example:

```bash
mvn clean verify sonar:sonar \
  -Dsonar.projectKey=alhusseinghader3_giftkeeper \
  -Dsonar.organization=YOUR_SONAR_ORGANIZATION \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.token=YOUR_SONAR_TOKEN
```

## 13. Final submission checklist

- `mvn clean verify` shows `BUILD SUCCESS`.
- Docker Desktop is open and Testcontainers tests run with `Skipped: 0`.
- JaCoCo reports are generated.
- PIT reports are generated.
- GitHub Actions workflow is green.
- SonarCloud Quality Gate is visible.
- `docs/FINAL_REPORT.md` is included.
- No `target/`, `.project`, `.classpath`, `.settings/`, `.idea/` or machine-specific files are committed.

## 14. Notes for the oral exam

Emphasize that the project is not only a CRUD application. Its academic value is the complete engineering pipeline: clean design, TDD-friendly domain, automated build, unit tests, mocks, integration tests with a real database, UI/E2E/BDD tests, coverage, mutation testing, CI and static quality analysis.

## 15. Final quality gate

Before final submission, follow `docs/FINAL_SUBMISSION_QUALITY_GATE.md` and collect the required screenshots/evidence.


## 16. Repository cleanliness

This final package intentionally excludes IDE-specific files such as `.project`, `.classpath` and `.settings/`. The repository should contain only portable source code, Maven configuration, tests, documentation, Docker files and CI/SonarCloud configuration.
