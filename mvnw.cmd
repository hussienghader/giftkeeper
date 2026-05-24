@echo off
REM Lightweight academic Maven wrapper fallback.
REM It uses the system Maven installation when the standard Maven wrapper JAR is not present.
where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
  mvn %*
  exit /b %ERRORLEVEL%
)
echo ERROR: Maven is not installed and the wrapper JAR is not bundled.
echo Install Maven 3.9+ or regenerate the wrapper with: mvn -N wrapper:wrapper -Dmaven=3.9.9
exit /b 1
