import type { NextFunction, Request, Response } from "express";
import * as jwt from "jsonwebtoken";

import { UnauthorizedError } from "@/core/errors/domain.error";
import type { AuthUser } from "@/core/types/auth-user.type";
import { env } from "@/infrastructure/config/env";

/**
 * Validates bearer JWT tokens and attaches the authenticated principal to req.user.
 */
export const authMiddleware = (req: Request, _res: Response, next: NextFunction) => {
  const authHeader = req.header("authorization");
  if (!authHeader) {
    next(new UnauthorizedError("Missing authorization header"));
    return;
  }

  const [scheme, token] = authHeader.split(" ");
  if (scheme !== "Bearer" || !token) {
    next(new UnauthorizedError("Invalid authorization header format"));
    return;
  }

  try {
    const decoded = jwt.verify(token, env.JWT_SECRET);
    if (typeof decoded === "string") {
      throw new UnauthorizedError("Invalid token payload");
    }

    const { sub, studentId } = decoded;
    if (typeof sub !== "string" || typeof studentId !== "string") {
      throw new UnauthorizedError("Invalid token payload");
    }

    req.user = { sub, studentId } satisfies AuthUser;
  } catch {
    next(new UnauthorizedError("Invalid or expired token"));
    return;
  }

  next();
};
