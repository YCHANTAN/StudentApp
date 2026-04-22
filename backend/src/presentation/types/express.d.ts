import type { AuthUser } from "@/core/types/auth-user.type";

declare global {
  namespace Express {
    interface Request {
      user?: AuthUser;
    }
  }
}

export {};
