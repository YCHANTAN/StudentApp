export class DomainError extends Error {
  constructor(
    message: string,
    public readonly code: string,
  ) {
    super(message);
    this.name = "DomainError";
  }
}

export class NotFoundError extends DomainError {
  constructor(resource: string) {
    super(`${resource} not found`, "NOT_FOUND");
  }
}

export class OutOfStockError extends DomainError {
  constructor(resource: string) {
    super(`${resource} has no available copies`, 'OUT_OF_STOCK');
  }
}

export class UserNotAuthorizedError extends DomainError {
  constructor(message: string) {
    super(message, 'UNAUTHORIZED_ACTION');
  }
}

export class ConflictError extends DomainError {
  constructor(message: string) {
    super(message, "CONFLICT");
  }
}
