import { Router } from "express";

import { LoginDto } from "@/application/dtos/auth.dto";
import { authController } from "@/container";
import { validate } from "@/presentation/middleware/validate.middleware";

export const authRouter = Router();

authRouter.post("/login", validate(LoginDto), authController.login);
