#!/usr/bin/env bash

set -eu

PROJECT_DIR=$(cd $(dirname $0)/..; pwd)

if [ -e "$PROJECT_DIR/.git" ]; then
  ln -sf ../../scripts/git-hooks/pre-commit .git/hooks/pre-commit
  chmod +x .git/hooks/pre-commit
else
  echo "  ! '$PROJECT_DIR/.git' is not found, can't link '.git/hooks/pre-commit'."
fi