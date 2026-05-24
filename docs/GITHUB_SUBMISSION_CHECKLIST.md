# GitHub Submission Checklist

Before pushing:

- [ ] Remove all `target/` folders if they exist.
- [ ] Run `mvn clean verify`.
- [ ] Run PIT mutation testing command from README.
- [ ] Keep Docker Desktop open for database integration evidence.
- [ ] Create screenshots of build, coverage, PIT, GitHub Actions and SonarCloud.

After pushing:

- [ ] Check GitHub Actions is green.
- [ ] Configure `SONAR_TOKEN` secret.
- [ ] Configure `SONAR_ORGANIZATION` variable.
- [ ] Check SonarCloud Quality Gate.
- [ ] Make sure README badges point to the correct GitHub username/project key.
