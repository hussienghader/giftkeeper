# Final SonarCloud cleanup

This version applies the final SonarCloud polish:

- GitHub Actions uses `fetch-depth: 0` to avoid shallow-clone warnings.
- CLI output uses `java.util.logging.Logger` and the CLI tests capture logger output correctly.
- Test variable names avoid restricted identifiers.
- AssertJ assertions use the Sonar-preferred forms.
- Pure API, BDD runner, and E2E orchestration modules remain part of the Maven build and tests, but are excluded/skipped from Sonar coverage because they do not contain production business logic.
- Sonar coverage now focuses on executable production code: domain, app, persistence-jpa, GUI, and CLI.

Validation command:

```powershell
mvn clean verify
```

Then push to GitHub and let GitHub Actions run the SonarCloud scan.
