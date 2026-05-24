$ErrorActionPreference = "Stop"

Write-Host "Cleaning old user-level Testcontainers/Docker variables..." -ForegroundColor Cyan
Remove-Item "$env:USERPROFILE\.testcontainers.properties" -Force -ErrorAction SilentlyContinue
Remove-Item Env:DOCKER_HOST -ErrorAction SilentlyContinue

Write-Host "Switching Docker CLI to Docker Desktop Linux engine..." -ForegroundColor Cyan
docker context use desktop-linux

docker version

docker run --rm hello-world

Write-Host "Done. Now run from the project root: mvn clean verify" -ForegroundColor Green
