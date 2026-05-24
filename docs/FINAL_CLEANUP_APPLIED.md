# Final Cleanup Applied

This package was cleaned for final academic submission and GitHub/SonarCloud upload.

Applied changes:

- Removed Eclipse/IDE-specific files: `.project`, `.classpath`, `.settings/`.
- Removed generated build output directories: `target/`.
- Removed explicit `hibernate.dialect` from `persistence.xml` because Hibernate 6 detects PostgreSQL automatically.
- Added marker source classes to `giftkeeper-bdd` and `giftkeeper-e2e` so Maven does not produce empty-JAR warnings while still running BDD/E2E tests normally.
- Strengthened `.gitignore` for Maven, IDEs, logs, OS files and generated coverage output.
- Kept the clean multi-module Maven structure required for the course.

Final local verification command:

```powershell
mvn clean verify
```

Expected final result:

```text
BUILD SUCCESS
Failures: 0
Errors: 0
Skipped: 0
```

For mutation testing evidence:

```powershell
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```
