import * as bcrypt from "bcryptjs";
import * as jwt from "jsonwebtoken";

import type { LoginInput, LoginResponse } from "@/application/dtos/auth.dto";
import type { StudentRepository } from "@/application/repositories/student.repository";
import { UnauthorizedError } from "@/core/errors/domain.error";
import { env } from "@/infrastructure/config/env";

/**
 * Authenticates a student and issues JWT tokens for API access.
 */
export class LoginUseCase {
  constructor(private readonly studentRepo: StudentRepository) {}

  async execute(input: LoginInput): Promise<LoginResponse> {
    const authRecord = await this.studentRepo.findAuthByStudentId(input.studentId);

    if (!authRecord?.passwordHash) {
      throw new UnauthorizedError("Invalid student ID or password");
    }

    const isPasswordValid = await bcrypt.compare(input.password, authRecord.passwordHash);
    if (!isPasswordValid) {
      throw new UnauthorizedError("Invalid student ID or password");
    }

    const payload = { sub: authRecord.id, studentId: authRecord.studentId };
    const accessSignOptions = { expiresIn: env.JWT_EXPIRES_IN } as jwt.SignOptions;
    const accessToken = jwt.sign(payload, env.JWT_SECRET, accessSignOptions);

    if (input.keepLoggedIn) {
      const refreshSignOptions = { expiresIn: env.JWT_REFRESH_EXPIRES_IN } as jwt.SignOptions;
      const refreshToken = jwt.sign(payload, env.JWT_SECRET, refreshSignOptions);

      return {
        accessToken,
        tokenType: "Bearer",
        expiresIn: env.JWT_EXPIRES_IN,
        refreshToken,
      };
    }

    return {
      accessToken,
      tokenType: "Bearer",
      expiresIn: env.JWT_EXPIRES_IN,
    };
  }
}
