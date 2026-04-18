# API_STANDARDS.md
> **Purpose:** This file defines the API response contract. Every endpoint in this project must conform to these shapes. When writing a controller, middleware, or error handler, treat this file as law.

---

## Core Principle

Every response — success or failure — returns the same top-level envelope. The client always knows what to expect.

```ts
type ApiResponse<T> =
  | { success: true;  data: T;      meta?: Meta }
  | { success: false; error: Error              }
```

- `success` is always a boolean.
- On success: `data` is present, `error` is absent.
- On failure: `error` is present, `data` is absent.
- Never mix the two. Never return raw objects at the top level.

---

## Success Response

### Single Resource
```json
{
  "success": true,
  "data": {
    "id": "abc-123",
    "name": "John Doe",
    "email": "john@example.com",
    "createdAt": "2024-01-15T10:30:00.000Z"
  }
}
```

### Collection (with pagination)
```json
{
  "success": true,
  "data": [
    { "id": "abc-123", "name": "John Doe", "email": "john@example.com" },
    { "id": "def-456", "name": "Jane Doe", "email": "jane@example.com" }
  ],
  "meta": {
    "total": 84,
    "page": 1,
    "limit": 20,
    "totalPages": 5
  }
}
```

### Mutation with no return body (e.g. DELETE)
```json
{
  "success": true,
  "data": null
}
```

> Always include `data`, even when null. Clients should not have to guard against a missing key.

---

## Error Response

### Shape
```json
{
  "success": false,
  "error": {
    "code": "MACHINE_READABLE_CODE",
    "message": "Human-readable description of what went wrong.",
    "details": {}
  }
}
```

- `code` — a screaming snake_case string. Used by clients to handle errors programmatically.
- `message` — a plain English sentence. Safe to display in logs; avoid exposing internals.
- `details` — optional. Used for field-level validation errors.

---

## Error Codes and HTTP Status Mapping

| Code | HTTP Status | When to use |
|---|---|---|
| `VALIDATION_ERROR` | 400 | Zod schema failed on request body / params / query |
| `BAD_REQUEST` | 400 | Malformed input that isn't a schema violation |
| `UNAUTHORIZED` | 401 | Missing or invalid authentication token |
| `FORBIDDEN` | 403 | Authenticated but not permitted to perform action |
| `NOT_FOUND` | 404 | Resource does not exist |
| `CONFLICT` | 409 | Duplicate resource or state conflict (e.g. email taken) |
| `UNPROCESSABLE` | 422 | Input is valid in shape but invalid in business logic |
| `RATE_LIMITED` | 429 | Too many requests |
| `INTERNAL_ERROR` | 500 | Unhandled exception — never expose internal details |

---

## Validation Error (Zod)

When `VALIDATION_ERROR` is returned, `details` contains field-level errors keyed by field name.

```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid request body",
    "details": {
      "email": ["Invalid email address"],
      "password": ["String must contain at least 8 character(s)"]
    }
  }
}
```

Produce this from Zod with:
```ts
result.error.flatten().fieldErrors
```

---

## HTTP Status Code Rules

| Scenario | Status |
|---|---|
| Resource created | `201 Created` |
| Read, update, or any other success | `200 OK` |
| Delete success | `200 OK` (with `data: null`) |
| Validation failed | `400 Bad Request` |
| Not authenticated | `401 Unauthorized` |
| Authenticated but not permitted | `403 Forbidden` |
| Resource not found | `404 Not Found` |
| State conflict | `409 Conflict` |
| Unhandled server error | `500 Internal Server Error` |

> Do not use `204 No Content`. Always return a body so the client envelope stays consistent.

---

## Pagination Convention

For any endpoint returning a collection, accept these query params:

| Param | Type | Default | Description |
|---|---|---|---|
| `page` | `number` | `1` | Current page (1-indexed) |
| `limit` | `number` | `20` | Items per page (max: 100) |

Validate with Zod:
```ts
export const PaginationDto = z.object({
  page: z.coerce.number().int().min(1).default(1),
  limit: z.coerce.number().int().min(1).max(100).default(20),
});
```

Always return the `meta` block on paginated responses:
```ts
type Meta = {
  total: number;       // total records in DB matching the query
  page: number;        // current page
  limit: number;       // items per page
  totalPages: number;  // Math.ceil(total / limit)
};
```

---

## Response Helper (Utility)

Centralise response construction so no controller builds raw objects:

```ts
// src/presentation/lib/response.helper.ts

type Meta = {
  total: number;
  page: number;
  limit: number;
  totalPages: number;
};

export const ok = <T>(res: Response, data: T, meta?: Meta) =>
  res.status(200).json({ success: true, data, ...(meta && { meta }) });

export const created = <T>(res: Response, data: T) =>
  res.status(201).json({ success: true, data });

export const noData = (res: Response) =>
  res.status(200).json({ success: true, data: null });

export const badRequest = (res: Response, message: string, details?: unknown) =>
  res.status(400).json({
    success: false,
    error: { code: 'BAD_REQUEST', message, ...(details && { details }) },
  });
```

Usage in a controller:
```ts
import { ok, created } from '@/presentation/lib/response.helper';

create = async (req: Request, res: Response, next: NextFunction) => {
  try {
    const user = await this.createUser.execute(req.body);
    created(res, user);
  } catch (err) {
    next(err);
  }
};
```

---

## Date and ID Conventions

- **Dates:** Always return ISO 8601 strings in UTC — `"2024-01-15T10:30:00.000Z"`.
- **IDs:** Use UUIDs (`crypto.randomUUID()`). Never expose auto-increment integers.
- **Nulls:** Return `null` explicitly. Never omit a key that is sometimes present.

---

## Versioning

- Prefix all routes with `/api/v1/`.
- When breaking changes are needed, introduce `/api/v2/` — never modify v1 in place.

---

## Security — What Never Goes in a Response

| Field | Reason |
|---|---|
| `password` / `passwordHash` | Obvious |
| Internal stack traces | Exposes implementation details |
| Database IDs (auto-increment) | Enumeration attacks |
| Internal service error messages | Exposes system internals |

Strip sensitive fields in the use case or with a dedicated mapper before returning from the repository. Do not rely on the controller to remember to delete them.
