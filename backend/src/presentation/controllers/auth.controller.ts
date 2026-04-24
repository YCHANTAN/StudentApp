import type { NextFunction, Request, Response } from "express";

import type { LoginUseCase } from "@/application/use-cases/auth/login.use-case";

/**
 * Handles HTTP transport concerns for authentication endpoints.
 */
export class AuthController {
  constructor(private readonly loginUseCase: LoginUseCase) {}

  /**
   * Authenticates a student and returns JWT credentials.
   */
  login = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const data = await this.loginUseCase.execute(req.body);
      res.status(200).json({ success: true, data });
    } catch (err) {
      next(err);
    }
  };

  me = async (req: Request, res: Response, next: NextFunction) => {
    try {
      // @ts-ignore - studentId added by authMiddleware
      res.status(200).json({ success: true, data: { studentId: req.user?.studentId } });
    } catch (err) {
      next(err);
    }
  };
}
