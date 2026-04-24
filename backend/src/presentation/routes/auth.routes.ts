import { Router } from "express";

import { LoginDto } from "@/application/dtos/auth.dto";
import { authController } from "@/container";
import { validate } from "@/presentation/middleware/validate.middleware";
import { authRateLimiter } from "@/presentation/middleware/rate-limit.middleware";

import { authMiddleware } from "@/presentation/middleware/auth.middleware";

export const authRouter = Router();

authRouter.post("/login", authRateLimiter, validate(LoginDto), authController.login);
authRouter.get("/me", authMiddleware, authController.me);
