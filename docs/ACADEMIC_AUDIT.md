# GiftKeeper Academic Audit

This document records the corrections applied to reach the final academically correct version.

## Corrections applied

### 1. `.gitignore` corrected — Eclipse metadata files kept in repository

The professor explicitly requires `.classpath`, `.project`, and `.settings/` to be committed
so the project can be imported directly as Eclipse projects.

Previous incorrect `.gitignore` listed these files as ignored. Corrected to:

```
target/
.idea/
*.iml
.factorypath
.DS_Store
.metadata/
*.log
*.tmp
.h2/
```

### 2. Testcontainers version corrected — 1.20.1

The project uses Testcontainers Java 1.20.1 with JUnit 5 and PostgreSQL support. The evidence log shows that PostgreSQL containers are started and that both integration and E2E tests run with `Skipped: 0`.

### 3. Testcontainers tests made strict

Integration and E2E tests now use `@Testcontainers` without `disabledWithoutDocker = true`. Docker is therefore an explicit requirement for the final verification: if PostgreSQL containers cannot start, the build fails instead of silently hiding the missing integration evidence.

### 4. CI simplified to avoid unnecessary external services

The CI workflow keeps Maven verification, JaCoCo reports, PIT mutation testing, uploaded artifacts and SonarCloud analysis. The optional Coveralls step was removed to avoid a non-course external dependency that could fail independently of the project quality.

### 5. All Eclipse metadata files committed

All 9 modules plus the root project have `.classpath`, `.project`, and `.settings/`
committed. The professor requires these so he can clone and import the project directly
in Eclipse without any manual configuration.

## Final verification

```bash
mvn clean verify
```

Expected with Docker running:

```
BUILD SUCCESS
Tests run: ..., Failures: 0, Errors: 0, Skipped: 0
```

Mutation testing:

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

### 6. Empty BOM module removed

The previous `giftkeeper-bom` module did not manage dependencies and added no academic value. It was removed to keep the Maven structure focused and easy to justify.
