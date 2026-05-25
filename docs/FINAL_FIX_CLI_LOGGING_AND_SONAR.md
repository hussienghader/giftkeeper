# Final CLI Logging and Sonar Polish

This version fixes the last local build failure caused by the CLI test expecting console output while the CLI was changed to use `java.util.logging.Logger` to satisfy SonarCloud clean-code expectations.

Applied changes:

- Kept `GiftKeeperCliMain` using `Logger.info(...)` instead of `System.out.println(...)`.
- Updated `GiftKeeperCliMainTest` to capture the CLI logger output using a dedicated in-test `Handler`.
- Preserved the SonarCloud cleanup that avoids the `System.out` code smell.
- Kept GitHub Actions checkout with `fetch-depth: 0`.
- Kept SonarCloud coverage exclusions for bootstrap/config/API/test-support modules.
- Kept README badges and SonarCloud project configuration.

Expected verification command:

```powershell
mvn clean verify
```

Then push:

```powershell
git add .
git commit -m "Fix CLI logging test and finalize Sonar polish"
git push
```
