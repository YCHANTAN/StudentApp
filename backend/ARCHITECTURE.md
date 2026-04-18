# ARCHITECTURE.md
> **Purpose:** This file is the single source of truth for folder structure, layer responsibilities, and module conventions. When scaffolding any new feature, follow this blueprint exactly.

---

## Stack
- **Runtime:** Node.js + TypeScript
- **Framework:** Express
- **ORM:** Drizzle
- **Database:** PostgreSQL
- **Validation:** Zod
- **Pattern:** Clean Architecture (Dependency Rule strictly enforced)

---

## Dependency Rule (Read This First)

```
Presentation  →  Application  →  Core
Infrastructure →  Application  →  Core
```

- `core/` has **zero** outward dependencies. No Express, no Drizzle, no Zod.
- `application/` depends only on `core/`. No Express, no Drizzle.
- `infrastructure/` implements interfaces defined in `application/`.
- `presentation/` calls use cases from `application/`. Never touches `infrastructure/` directly.
- All wiring happens in `container.ts`.

Breaking this rule means the architecture is broken. Do not import across layers out of order.

---

## Full Folder Structure

```
src/
├── core/                               # Layer 1 — Enterprise business rules
│   ├── entities/                       # Pure domain models. No frameworks, no decorators.
│   │   └── user.entity.ts
│   ├── errors/                         # Domain-specific error classes
│   │   └── domain.error.ts
│   └── types/                          # Shared value types used across the domain
│       └── pagination.type.ts
│
├── application/                        # Layer 2 — Application business rules
│   ├── use-cases/                      # One folder per domain, one file per use case
│   │   └── user/
│   │       ├── create-user.use-case.ts
│   │       ├── get-user.use-case.ts
│   │       ├── update-user.use-case.ts
│   │       └── delete-user.use-case.ts
│   ├── repositories/                   # Repository interfaces (abstractions, not implementations)
│   │   └── user.repository.ts
│   └── dtos/                           # Zod schemas for input/output contracts
│       └── user.dto.ts
│
├── infrastructure/                     # Layer 3 — Frameworks, DB, external services
│   ├── db/
│   │   ├── schema/                     # Drizzle table definitions
│   │   │   └── user.schema.ts
│   │   ├── repositories/               # Concrete implementations of application interfaces
│   │   │   └── user.pg.repository.ts
│   │   ├── migrations/                 # Drizzle-generated migration files (do not edit manually)
│   │   └── client.ts                   # Drizzle client singleton — import this everywhere in infra
│   └── config/
│       └── env.ts                      # Zod-parsed and validated environment variables
│
├── presentation/                       # Layer 4 — HTTP interface (Express)
│   ├── routes/                         # Route definitions. Only wires path + middleware + controller.
│   │   └── user.routes.ts
│   ├── controllers/                    # Thin handlers. Calls one use case. Returns HTTP response.
│   │   └── user.controller.ts
│   └── middleware/
│       ├── validate.middleware.ts      # Zod schema validation for req.body / req.params / req.query
│       ├── error.middleware.ts         # Global error handler. All thrown errors land here.
│       └── auth.middleware.ts          # JWT or session verification
│
├── container.ts                        # Dependency injection root. Wires infra → application → presentation.
├── app.ts                              # Express app setup. Registers routes and middleware.
└── main.ts                             # Entry point. Starts the HTTP server.
```

---

## Layer Responsibilities (Per File Type)

### `core/entities/*.entity.ts`
- Plain TypeScript types or interfaces only.
- Represents the domain object — what a "User" IS, not how it's stored or transferred.
- No Zod, no Drizzle, no class decorators.

```ts
// core/entities/user.entity.ts
export type User = {
  id: string;
  email: string;
  name: string;
  createdAt: Date;
};
```

---

### `core/errors/*.error.ts`
- Custom error classes that carry domain meaning.
- Used inside use cases to signal known failure states.

```ts
// core/errors/domain.error.ts
export class DomainError extends Error {
  constructor(
    message: string,
    public readonly code: string,
  ) {
    super(message);
    this.name = 'DomainError';
  }
}

export class NotFoundError extends DomainError {
  constructor(resource: string) {
    super(`${resource} not found`, 'NOT_FOUND');
  }
}

export class ConflictError extends DomainError {
  constructor(message: string) {
    super(message, 'CONFLICT');
  }
}
```

---

### `application/repositories/*.repository.ts`
- TypeScript interface only — no implementation.
- Defines what persistence operations the use case needs.
- Named after the domain entity, not the database.

```ts
// application/repositories/user.repository.ts
import type { User } from '@/core/entities/user.entity';

export interface UserRepository {
  findById(id: string): Promise<User | null>;
  findByEmail(email: string): Promise<User | null>;
  findAll(): Promise<User[]>;
  save(user: User): Promise<User>;
  update(id: string, data: Partial<User>): Promise<User>;
  delete(id: string): Promise<void>;
}
```

---

### `application/dtos/*.dto.ts`
- Zod schemas that define the shape of data entering or leaving a use case.
- Export both the Zod schema and its inferred TypeScript type.
- Do not define HTTP-specific concerns here (no status codes, no headers).

```ts
// application/dtos/user.dto.ts
import { z } from 'zod';

export const CreateUserDto = z.object({
  name: z.string().min(1),
  email: z.string().email(),
  password: z.string().min(8),
});

export const UpdateUserDto = z.object({
  name: z.string().min(1).optional(),
  email: z.string().email().optional(),
});

export type CreateUserInput = z.infer<typeof CreateUserDto>;
export type UpdateUserInput = z.infer<typeof UpdateUserDto>;
```

---

### `application/use-cases/**/*.use-case.ts`
- One class per file. One `execute()` method per class.
- Receives dependencies via constructor (injected from `container.ts`).
- Never imports Express, Drizzle, or any framework.
- All business logic lives here.

```ts
// application/use-cases/user/create-user.use-case.ts
import type { UserRepository } from '@/application/repositories/user.repository';
import type { CreateUserInput } from '@/application/dtos/user.dto';
import type { User } from '@/core/entities/user.entity';
import { ConflictError } from '@/core/errors/domain.error';

export class CreateUserUseCase {
  constructor(private readonly userRepo: UserRepository) {}

  async execute(input: CreateUserInput): Promise<User> {
    const existing = await this.userRepo.findByEmail(input.email);
    if (existing) throw new ConflictError('Email already in use');

    const user: User = {
      id: crypto.randomUUID(),
      email: input.email,
      name: input.name,
      createdAt: new Date(),
    };

    return this.userRepo.save(user);
  }
}
```

---

### `infrastructure/db/schema/*.schema.ts`
- Drizzle table definitions only.
- Column names use snake_case (database convention).
- Export the table and its inferred select/insert types.

```ts
// infrastructure/db/schema/user.schema.ts
import { pgTable, text, timestamp } from 'drizzle-orm/pg-core';

export const users = pgTable('users', {
  id: text('id').primaryKey(),
  email: text('email').notNull().unique(),
  name: text('name').notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type UserRow = typeof users.$inferSelect;
export type InsertUserRow = typeof users.$inferInsert;
```

---

### `infrastructure/db/repositories/*.pg.repository.ts`
- Implements the repository interface from `application/repositories/`.
- Only file allowed to import Drizzle and the DB client.
- Maps between `UserRow` (DB shape) and `User` (domain entity) if they differ.

```ts
// infrastructure/db/repositories/user.pg.repository.ts
import { eq } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { UserRepository } from '@/application/repositories/user.repository';
import type { User } from '@/core/entities/user.entity';
import { users } from '@/infrastructure/db/schema/user.schema';

export class UserPgRepository implements UserRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findById(id: string): Promise<User | null> {
    const [row] = await this.db.select().from(users).where(eq(users.id, id));
    return row ?? null;
  }

  async findByEmail(email: string): Promise<User | null> {
    const [row] = await this.db.select().from(users).where(eq(users.email, email));
    return row ?? null;
  }

  async findAll(): Promise<User[]> {
    return this.db.select().from(users);
  }

  async save(user: User): Promise<User> {
    const [row] = await this.db.insert(users).values(user).returning();
    return row;
  }

  async update(id: string, data: Partial<User>): Promise<User> {
    const [row] = await this.db.update(users).set(data).where(eq(users.id, id)).returning();
    return row;
  }

  async delete(id: string): Promise<void> {
    await this.db.delete(users).where(eq(users.id, id));
  }
}
```

---

### `presentation/controllers/*.controller.ts`
- Thin HTTP handler. Extract input → call use case → return response.
- Never contains business logic.
- One method per use case. Uses arrow functions to preserve `this`.
- Delegates all error handling to `error.middleware.ts` via `next(err)`.

```ts
// presentation/controllers/user.controller.ts
import type { Request, Response, NextFunction } from 'express';
import type { CreateUserUseCase } from '@/application/use-cases/user/create-user.use-case';
import type { GetUserUseCase } from '@/application/use-cases/user/get-user.use-case';

export class UserController {
  constructor(
    private readonly createUser: CreateUserUseCase,
    private readonly getUser: GetUserUseCase,
  ) {}

  create = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const user = await this.createUser.execute(req.body);
      res.status(201).json({ success: true, data: user });
    } catch (err) {
      next(err);
    }
  };

  findById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const user = await this.getUser.execute(req.params.id);
      res.status(200).json({ success: true, data: user });
    } catch (err) {
      next(err);
    }
  };
}
```

---

### `presentation/routes/*.routes.ts`
- Registers paths, applies middleware, binds controller methods.
- No logic here — just wiring.

```ts
// presentation/routes/user.routes.ts
import { Router } from 'express';
import { validate } from '@/presentation/middleware/validate.middleware';
import { CreateUserDto } from '@/application/dtos/user.dto';
import { userController } from '@/container';

export const userRouter = Router();

userRouter.post('/', validate(CreateUserDto), userController.create);
userRouter.get('/:id', userController.findById);
```

---

### `presentation/middleware/validate.middleware.ts`
- Validates `req.body` against a Zod schema before the controller runs.
- On failure: returns 400 immediately.
- On success: replaces `req.body` with the parsed (type-safe) data.

```ts
// presentation/middleware/validate.middleware.ts
import type { Request, Response, NextFunction } from 'express';
import type { ZodSchema } from 'zod';

export const validate =
  (schema: ZodSchema) => (req: Request, res: Response, next: NextFunction) => {
    const result = schema.safeParse(req.body);
    if (!result.success) {
      res.status(400).json({
        success: false,
        error: {
          code: 'VALIDATION_ERROR',
          message: 'Invalid request body',
          details: result.error.flatten().fieldErrors,
        },
      });
      return;
    }
    req.body = result.data;
    next();
  };
```

---

### `presentation/middleware/error.middleware.ts`
- The global error handler — last middleware registered in `app.ts`.
- Maps `DomainError` codes to HTTP status codes.
- All unknown errors become 500.

```ts
// presentation/middleware/error.middleware.ts
import type { Request, Response, NextFunction } from 'express';
import { DomainError } from '@/core/errors/domain.error';

const statusMap: Record<string, number> = {
  NOT_FOUND: 404,
  CONFLICT: 409,
  FORBIDDEN: 403,
  UNAUTHORIZED: 401,
};

export const errorMiddleware = (
  err: unknown,
  _req: Request,
  res: Response,
  _next: NextFunction,
) => {
  if (err instanceof DomainError) {
    const status = statusMap[err.code] ?? 400;
    res.status(status).json({
      success: false,
      error: { code: err.code, message: err.message },
    });
    return;
  }

  console.error('[Unhandled Error]', err);
  res.status(500).json({
    success: false,
    error: { code: 'INTERNAL_ERROR', message: 'An unexpected error occurred' },
  });
};
```

---

### `container.ts`
- The only file that knows about all layers simultaneously.
- Instantiates infrastructure → use cases → controllers.
- Keep it flat and explicit. No magic, no IoC containers unless the project grows large.

```ts
// container.ts
import { db } from '@/infrastructure/db/client';
import { UserPgRepository } from '@/infrastructure/db/repositories/user.pg.repository';
import { CreateUserUseCase } from '@/application/use-cases/user/create-user.use-case';
import { GetUserUseCase } from '@/application/use-cases/user/get-user.use-case';
import { UserController } from '@/presentation/controllers/user.controller';

// Repositories
const userRepo = new UserPgRepository(db);

// Use Cases
const createUserUseCase = new CreateUserUseCase(userRepo);
const getUserUseCase = new GetUserUseCase(userRepo);

// Controllers
export const userController = new UserController(createUserUseCase, getUserUseCase);
```

---

### `app.ts`

```ts
// app.ts
import express from 'express';
import { userRouter } from '@/presentation/routes/user.routes';
import { errorMiddleware } from '@/presentation/middleware/error.middleware';

export const app = express();

app.use(express.json());

// Routes
app.use('/api/v1/users', userRouter);

// Global error handler — must be last
app.use(errorMiddleware);
```

---

### `main.ts`

```ts
// main.ts
import { app } from './app';
import { env } from '@/infrastructure/config/env';

app.listen(env.PORT, () => {
  console.log(`Server running on port ${env.PORT}`);
});
```

---

## Naming Conventions

| Artifact | Convention | Example |
|---|---|---|
| Entity | `*.entity.ts` | `user.entity.ts` |
| Repository interface | `*.repository.ts` | `user.repository.ts` |
| Repository implementation | `*.pg.repository.ts` | `user.pg.repository.ts` |
| Use case | `*.use-case.ts` | `create-user.use-case.ts` |
| DTO | `*.dto.ts` | `user.dto.ts` |
| Schema (Drizzle) | `*.schema.ts` | `user.schema.ts` |
| Controller | `*.controller.ts` | `user.controller.ts` |
| Route | `*.routes.ts` | `user.routes.ts` |
| Middleware | `*.middleware.ts` | `validate.middleware.ts` |

---

## Adding a New Domain (LLM Instruction)

When asked to scaffold a new domain (e.g., `product`), create these files in order:

1. `src/core/entities/product.entity.ts` — define the domain type
2. `src/application/repositories/product.repository.ts` — define the interface
3. `src/application/dtos/product.dto.ts` — define Zod input schemas
4. `src/application/use-cases/product/create-product.use-case.ts` — one use case per operation
5. `src/infrastructure/db/schema/product.schema.ts` — Drizzle table
6. `src/infrastructure/db/repositories/product.pg.repository.ts` — implement the interface
7. `src/presentation/controllers/product.controller.ts` — HTTP handler
8. `src/presentation/routes/product.routes.ts` — register routes
9. `src/container.ts` — wire the new dependency chain
10. `src/app.ts` — register the new router

Do not skip steps. Do not merge files across layers.
