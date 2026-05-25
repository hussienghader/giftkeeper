# Final SonarCloud and GitHub Polish Applied

This package includes the requested final professional clean-up:

- SonarCloud shallow clone warning fixed by setting `fetch-depth: 0` in GitHub Actions.
- SonarCloud coverage exclusions refined for bootstrap, launcher, module, API marker, BDD and E2E infrastructure classes.
- CLI output changed from direct `System.out` usage to `java.util.logging.Logger`.
- Test assertions cleaned to satisfy SonarCloud maintainability rules.
- Mockito `eq(...)` wrappers removed where direct values are clearer and sufficient.
- README badges added for CI, Quality Gate, Coverage, Bugs and Vulnerabilities.

Recommended verification commands:

```powershell
mvn clean verify
git add .
git commit -m "Polish SonarCloud configuration and README badges"
git push
```
