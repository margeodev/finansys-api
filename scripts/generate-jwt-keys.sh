#!/usr/bin/env bash
# Gera par de chaves RSA para JWT (uso local).
# Execução: ./scripts/generate-jwt-keys.sh

set -e
KEYS_DIR="$(cd "$(dirname "$0")/.." && pwd)/keys"
mkdir -p "$KEYS_DIR"

openssl genrsa -out "$KEYS_DIR/private.pem" 2048
openssl rsa -in "$KEYS_DIR/private.pem" -pubout -out "$KEYS_DIR/public.pem"

echo "Chaves geradas em: $KEYS_DIR"
echo "  - private.pem"
echo "  - public.pem"
echo ""
echo "Rode a API com perfil local:"
echo "  mvn spring-boot:run -Dspring-boot.run.profiles=local"
echo ""
echo "Ou com a IDE: active o profile 'local'."
