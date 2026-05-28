# GiftKeeper Setup Notes

## Local verification

```bash
mvn clean verify
```

Docker must be running because JPA integration and E2E tests use PostgreSQL through Testcontainers.

## GUI

```bash
docker compose -f docker/docker-compose.yml up -d
mvn -pl giftkeeper-gui -am exec:java -Dexec.mainClass=com.giftkeeper.gui.GiftKeeperGuiMain
```

## CLI

```bash
mvn -pl giftkeeper-cli -am exec:java -Dexec.mainClass=com.giftkeeper.cli.GiftKeeperCliMain
```

## Mutation testing

```bash
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage
```

## SonarCloud

Disable Automatic Analysis in SonarCloud and let GitHub Actions perform the analysis after JaCoCo XML reports are generated.
