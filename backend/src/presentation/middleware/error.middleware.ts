import type { NextFunction, Request, Response } from "express";

import { DomainError } from "@/core/errors/domain.error";

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
    const domainErr = err;
    const status = statusMap[domainErr.code] ?? 400;
    res.status(status).json({
      success: false,
      error: { code: domainErr.code, message: domainErr.message },
    });
    return;
  }

  console.error("[Unhandled Error]", err);
  res.status(500).json({
    success: false,
    error: { code: "INTERNAL_ERROR", message: "An unexpected error occurred" },
  });
};
