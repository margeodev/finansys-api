# Finansys API

API REST para controle financeiro (despesas, categorias, relatórios) com autenticação JWT.

## Requisitos

- Java 21
- Maven 3.9+

Para rodar **sem** perfil local: MySQL e variáveis de ambiente (ver abaixo).

## Rodar localmente (sem MySQL)

1. **Gerar chaves JWT** (uma vez):
   ```bash
   chmod +x scripts/generate-jwt-keys.sh
   ./scripts/generate-jwt-keys.sh
   ```
   Isso cria `keys/private.pem` e `keys/public.pem` (a pasta `keys/` está no `.gitignore`).

2. **Subir a aplicação** com o perfil `local` (usa H2 em memória):
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

3. A API fica em **http://localhost:8080**.
   - Console H2 (para ver o banco): http://localhost:8080/h2-console  
   - JDBC URL: `jdbc:h2:mem:finansys`  
   - User: `sa` / Password: (vazio)

### Login

- **POST** `/auth/login` com HTTP Basic: usuário = **e-mail** do usuário, senha = senha.
- É necessário ter um usuário ativo no banco (por exemplo criado via H2 console ou um script de carga).

## Rodar com MySQL

Defina as variáveis de ambiente e use a aplicação **sem** o perfil `local`:

- `DATASOURCE_URL` – host do MySQL (ex: `localhost`)
- `PORT_NUMBER` – porta (ex: `3306`)
- `DATABASE` – nome do banco
- `MY_SQL_USER` / `MY_SQL_PASSWORD`
- `JWT_PUBLIC_KEY` / `JWT_PRIVATE_KEY` – conteúdo das chaves RSA (ou no perfil local use `file:./keys/public.pem` e `file:./keys/private.pem`)
- `ALLOWED_ORIGINS` – origens CORS permitidas (ex: `http://localhost:3000`)

Depois:
```bash
mvn spring-boot:run
```

## Endpoints principais

| Método | Caminho | Descrição |
|--------|---------|------------|
| POST | `/auth/login` | Login (Basic Auth) → retorna access + refresh token |
| POST | `/auth/refresh` | Renova access token com o refresh token |
| * | `/api/v1/expenses` | CRUD de despesas (exige JWT) |
| * | `/api/v1/reports` | Relatórios (exige JWT) |
| * | `/api/v1/categories` | Categorias (exige JWT) |
| * | `/api/v1/users` | Usuários (exige JWT) |

## Docker

```bash
docker build -t finansys-api .
docker run -p 8080:8080 --env-file .env finansys-api
```

(Use um arquivo `.env` com as variáveis listadas acima.)
