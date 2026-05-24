#!/usr/bin/env bash
set -euo pipefail

echo "GiftKeeper local verification"
echo "1) Checking Java, Maven and Docker..."
java -version
mvn -version
docker version

echo "2) Running full Maven verification..."
mvn clean verify

echo "3) Checking that no automated tests were skipped..."
python3 - <<'PY'
from pathlib import Path
import xml.etree.ElementTree as ET
skipped = 0
for report in Path('.').rglob('target/surefire-reports/TEST-*.xml'):
    root = ET.parse(report).getroot()
    skipped += int(root.attrib.get('skipped', '0'))
if skipped:
    raise SystemExit(f'Verification failed: {skipped} tests were skipped. Integration/E2E tests must run for final academic evidence.')
print('No skipped tests detected.')
PY

echo "4) Running PIT mutation testing with required modules..."
mvn -pl giftkeeper-domain,giftkeeper-persistence-api,giftkeeper-persistence-jpa,giftkeeper-app -am install -DskipTests
mvn -pl giftkeeper-domain,giftkeeper-persistence-jpa,giftkeeper-app pitest:mutationCoverage

echo "Verification completed. Check target/site/jacoco and target/pit-reports folders."
