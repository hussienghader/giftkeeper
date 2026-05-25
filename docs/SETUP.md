# GiftKeeper Setup and Verification Guide

## Requirements

- Java 17
- Maven 3.9+
- Docker Desktop or Docker Engine
- Git

## Windows/Testcontainers note

If Docker works from the terminal but Testcontainers fails, clean old user-level Testcontainers configuration:

```powershell
.\scripts\fix-testcontainers-windows.ps1
```

The corrected project also includes module-level `testcontainers.properties` and `docker-java.properties` files to ignore stale user configuration and use a Docker API version compatible with recent Docker Desktop releases.

## Verify the project locally

```bash
mvn clean verify
```

Docker must be running and the result must show no skipped tests:

```text
BUILD SUCCESS
Failures: 0, Errors: 0, Skipped: 0
```

You can use the stricter helper script:

```powershell
.\scripts\verify-local.ps1
```

or:

```bash
./scripts/verify-local.sh
```

## Run mutation testing

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

## Start PostgreSQL manually

```bash
docker compose -f docker/docker-compose.yml up -d
```

## Stop PostgreSQL

```bash
docker compose -f docker/docker-compose.yml down -v
```

## Run GUI

```bash
mvn -pl giftkeeper-gui -am exec:java -Dexec.mainClass=com.giftkeeper.gui.GiftKeeperGuiMain
```

## Run CLI

```bash
mvn -pl giftkeeper-cli -am exec:java -Dexec.mainClass=com.giftkeeper.cli.GiftKeeperCliMain
```

## Push to GitHub

```bash
git init
git add .
git commit -m "Initial GiftKeeper final academic project"
git branch -M main
git remote add origin https://github.com/hussienghader/giftkeeper.git
git push -u origin main
```

## SonarCloud setup

1. Create the project in SonarCloud.
2. Use project key: `hussienghader_giftkeeper` or update the README/workflow to your chosen key.
3. In GitHub repository settings, add secret `SONAR_TOKEN`.
4. Add repository variable `SONAR_ORGANIZATION`.
5. Push again and check GitHub Actions.

## Evidence to collect for submission

- Screenshot of `mvn clean verify` with `BUILD SUCCESS` and `Skipped: 0`.
- Screenshot of Testcontainers starting PostgreSQL.
- Screenshot of JaCoCo HTML/XML report.
- Screenshot of PIT mutation report.
- Screenshot of GitHub Actions green workflow.
- Screenshot of SonarCloud Quality Gate.
