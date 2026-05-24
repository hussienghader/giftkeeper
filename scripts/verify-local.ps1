$ErrorActionPreference = "Stop"

Write-Host "GiftKeeper local verification" -ForegroundColor Cyan
Write-Host "1) Checking Java, Maven and Docker..." -ForegroundColor Cyan
java -version
mvn -version
docker version

Write-Host "2) Running full Maven verification..." -ForegroundColor Cyan
mvn clean verify

Write-Host "3) Checking that no automated tests were skipped..." -ForegroundColor Cyan
$reports = Get-ChildItem -Path . -Recurse -Filter "TEST-*.xml" | Where-Object { $_.FullName -like "*target*surefire-reports*" }
$skipped = 0
foreach ($report in $reports) {
    [xml]$xml = Get-Content $report.FullName
    if ($xml.testsuite.skipped) {
        $skipped += [int]$xml.testsuite.skipped
    }
}
if ($skipped -gt 0) {
    throw "Verification failed: $skipped tests were skipped. Integration/E2E tests must run for final academic evidence."
}
Write-Host "No skipped tests detected." -ForegroundColor Green

Write-Host "4) Running PIT mutation testing with required modules..." -ForegroundColor Cyan
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage

Write-Host "Verification completed. Check target/site/jacoco and target/pit-reports folders." -ForegroundColor Green
