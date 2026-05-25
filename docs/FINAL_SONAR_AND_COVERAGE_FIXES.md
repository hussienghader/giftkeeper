# Final SonarCloud and Coverage Fixes

This revision focuses on the last SonarCloud problems observed in the project dashboard:

- Quality Gate failed because New Code coverage was reported as 0% on a small number of new executable lines.
- Overall coverage was lower than the local JaCoCo module reports because SonarCloud was not consistently receiving/using all module JaCoCo XML reports.
- Two remaining Code Smells were visible in SonarCloud: one related to CLI output and one related to test hash-code assertion style.

## What was fixed

1. **JaCoCo XML import path**
   The SonarCloud configuration now uses the robust wildcard path:

   ```properties
   sonar.coverage.jacoco.xmlReportPaths=**/target/site/jacoco/jacoco.xml
   ```

   This avoids missing one or more module reports in the GitHub Actions environment.

2. **Coverage exclusions normalized**
   Coverage exclusions were placed as one-line Maven/Sonar properties to ensure SonarScanner for Maven applies them correctly. They exclude only non-business or orchestration code such as:

   - `*Main.java`
   - `*Application.java`
   - `*Module.java`
   - repository interfaces/ports
   - BDD/E2E orchestration modules
   - JPA bootstrap factory code

   The executable business and persistence logic remains analyzed and covered by tests.

3. **GitHub Actions shallow clone fixed**
   `actions/checkout@v4` uses `fetch-depth: 0`, which removes the SonarCloud shallow-clone warning.

4. **CLI Sonar issue fixed without breaking tests**
   The CLI uses `java.util.logging.Logger`, and the test captures the logger through a custom handler instead of relying on console output.

5. **AssertJ hash-code issue fixed**
   Tests compare hash-code values directly instead of using a rule-triggering assertion style.

## Expected result after pushing

After running `mvn clean verify`, committing, and pushing to GitHub, GitHub Actions should:

1. Build all Maven modules.
2. Run unit, integration, BDD, GUI, and E2E tests.
3. Generate JaCoCo XML reports.
4. Send the reports to SonarCloud.
5. Pass the Quality Gate with no remaining project-blocking issues.

If SonarCloud still shows old issues, wait for the latest analysis to finish and make sure the dashboard is showing the latest commit hash.
