# Final Sonar and Coverage Cleanup

This version applies the final academic polish:

- GitHub Actions uses `fetch-depth: 0` to avoid shallow-clone warnings in SonarCloud.
- Sonar coverage excludes entry points, dependency-injection modules, interfaces/ports, catalog interfaces, BDD/E2E bootstrap modules, and persistence API ports from coverage calculations. These classes are not business logic and are standard exclusions in professional projects.
- Remaining Sonar maintainability issues in tests were cleaned where applicable.
- The local Maven verification command remains `mvn clean verify`.

Run:

```powershell
mvn clean verify
git add .
git commit -m "Finalize Sonar coverage cleanup"
git push
```
