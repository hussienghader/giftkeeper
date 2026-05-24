#!/usr/bin/env bash
# Safe GitHub setup script. It does not store tokens in the remote URL.
# Usage: ./setup-git.sh your-github-username

set -euo pipefail

USERNAME=${1:-alhusseinghader3}
REPO=${2:-giftkeeper}

if [ ! -d .git ]; then
  git init
fi

git branch -M main

if ! git remote get-url origin >/dev/null 2>&1; then
  git remote add origin "https://github.com/${USERNAME}/${REPO}.git"
else
  git remote set-url origin "https://github.com/${USERNAME}/${REPO}.git"
fi

git add .
git commit -m "Initial GiftKeeper final academic project" || true

echo "Remote configured: https://github.com/${USERNAME}/${REPO}.git"
echo "Now run: git push -u origin main"
echo "If Git asks for authentication, use GitHub login or a Personal Access Token through Git Credential Manager."
