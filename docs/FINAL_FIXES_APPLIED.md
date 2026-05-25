# Final Fixes Applied

This version was cleaned and polished for final academic submission.

Applied changes:

- Added complete unit coverage for `RuleBasedGiftSuggestionCatalog`.
- Kept domain, persistence, CLI, and GUI modules at full local JaCoCo coverage.
- Fixed GUI Sonar warnings by using `WindowConstants.DISPOSE_ON_CLOSE` and keeping `serialVersionUID`.
- Suppressed Sonar rule `java:S5778` only in test classes where several constructor argument preparations are intentionally used for exception assertions.
- Standardized SonarCloud project metadata to `hussienghader_giftkeeper` / `hussienghader`.
- Updated GitHub Actions workflow for CI-based SonarCloud analysis.
- Removed generated `target/` directories, local Git metadata, and IDE-specific files from the distributable archive.
- Removed broken Maven Wrapper files because the wrapper JAR was not bundled; the project now uses the standard installed Maven command `mvn`.

Recommended final verification commands:

```powershell
mvn clean verify
.\scripts\verify-local.ps1
```

Then push:

```powershell
git add .
git commit -m "Polish final academic submission"
git push
```
