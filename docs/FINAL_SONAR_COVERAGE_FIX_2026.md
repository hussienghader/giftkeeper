# Final SonarCloud Coverage Fix

This version focuses on the real issue observed in SonarCloud: GitHub Actions was running the analysis, but SonarCloud was not reliably importing the Maven module JaCoCo XML reports for all modules.

Applied fixes:

1. GitHub Actions now uses `fetch-depth: 0` to avoid shallow-clone warnings.
2. GitHub Actions prints all generated JaCoCo XML reports before the SonarCloud scan.
3. SonarCloud scan receives explicit absolute paths to each business module JaCoCo XML file:
   - `giftkeeper-domain/target/site/jacoco/jacoco.xml`
   - `giftkeeper-app/target/site/jacoco/jacoco.xml`
   - `giftkeeper-persistence-jpa/target/site/jacoco/jacoco.xml`
   - `giftkeeper-gui/target/site/jacoco/jacoco.xml`
   - `giftkeeper-cli/target/site/jacoco/jacoco.xml`
4. Non-business launchers, DI modules, interfaces/ports, BDD wrappers, and E2E wrappers are excluded from the coverage denominator.
5. Test-only intentional Sonar rules are ignored only for test sources.

After pushing this version, verify in GitHub Actions logs that the step **Show generated JaCoCo XML reports** lists the XML files. Then check SonarCloud after the scan completes.
