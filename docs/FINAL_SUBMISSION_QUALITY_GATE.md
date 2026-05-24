# Final Submission Quality Gate

This checklist is used before pushing the final version to GitHub and linking SonarCloud.

## Mandatory evidence

- `mvn clean verify` or `./scripts/verify-local.sh` finishes with `BUILD SUCCESS`.
- All test reports show `Failures: 0`, `Errors: 0`, `Skipped: 0`.
- Testcontainers starts PostgreSQL for:
  - `JpaRepositoriesIntegrationTest`
  - `GiftKeeperEndToEndTest`
- PIT mutation testing runs for domain, application and JPA modules.
- JaCoCo XML/HTML reports are generated.
- GitHub Actions workflow is green.
- SonarCloud Quality Gate is green.

## Design evidence

- Domain layer has no dependency on UI, JPA, Docker or frameworks.
- Persistence is accessed through repository interfaces.
- JPA implementation is isolated in `giftkeeper-persistence-jpa`.
- GUI and CLI use the application layer.
- BDD/E2E tests demonstrate user-level behavior rather than isolated methods.

## Cleanliness evidence

- No generated `target/` folders are committed.
- No secrets are committed.
- No optional external services are required except SonarCloud when configured.
- Eclipse metadata is intentionally kept for direct import, as required by the academic context.
