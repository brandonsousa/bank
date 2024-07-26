#!/bin/bash

wait_for_db() {
  local host="$1"
  local port="$2"
  local max_attempts=30
  local attempt=1

  echo "Aguardando o banco de dados estar disponível..."

  while ! nc -z "$host" "$port"; do
    if [ "$attempt" -ge "$max_attempts" ]; then
      echo "Banco de dados não está disponível após $max_attempts tentativas. Abortando."
      exit 1
    fi
    echo "Tentativa $attempt/$max_attempts: Banco de dados não disponível. Aguardando..."
    sleep 5
    attempt=$((attempt + 1))
  done

  echo "Banco de dados está disponível!"
}

wait_for_db "db" 5432

./mvnw liquibase:update -Pdocker

exec java -jar app.jar --spring.profiles.active=docker