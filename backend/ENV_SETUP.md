# Environment Setup

## What to avoid in .env

- Do not commit real secrets to git.
- Do not reuse production passwords or tokens in local development.
- Do not use weak secrets (JWT secret must be long and random).
- Do not share `.env` in chat, screenshots, or issue trackers.
- Do not hardcode credentials in source files.

## Recommended practice

- Keep `.env` for local machine only.
- Commit `.env.example` with placeholders only.
- Rotate secrets immediately if leaked.
- Use separate credentials per environment (dev/staging/prod).

## Local PostgreSQL setup (no Docker)

1. Install PostgreSQL locally (Windows installer from postgresql.org).
2. During installation, remember the superuser password for `postgres`.
3. Open `psql` or pgAdmin and run:

```sql
CREATE ROLE studentapp WITH LOGIN PASSWORD 'studentapp_dev_password';
CREATE DATABASE studentapp OWNER studentapp;
GRANT ALL PRIVILEGES ON DATABASE studentapp TO studentapp;
```

4. Put this in `.env`:

```env
DATABASE_URL=postgresql://studentapp:studentapp_dev_password@localhost:5432/studentapp
```

5. Generate and apply schema migrations:

```bash
npm run db:generate
npm run db:migrate
```

6. Start API:

```bash
npm run dev
```

## Swagger API Docs

- UI: `http://localhost:3000/api/docs`
- OpenAPI JSON: `http://localhost:3000/api/docs.json`
