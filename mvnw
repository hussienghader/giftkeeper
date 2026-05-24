#!/usr/bin/env sh
# Lightweight academic Maven wrapper fallback.
# It uses the system Maven installation when the standard Maven wrapper JAR is not present.
set -e
if command -v mvn >/dev/null 2>&1; then
  exec mvn "$@"
fi
echo "ERROR: Maven is not installed and the wrapper JAR is not bundled." >&2
echo "Install Maven 3.9+ or regenerate the wrapper with: mvn -N wrapper:wrapper -Dmaven=3.9.9" >&2
exit 1
