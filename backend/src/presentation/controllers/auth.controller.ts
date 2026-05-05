import type { NextFunction, Request, Response } from "express";

import type { LoginUseCase } from "@/application/use-cases/auth/login.use-case";
import type { GetStudentUseCase } from "@/application/use-cases/student/get-student.use-case";

/**
 * Handles HTTP transport concerns for authentication endpoints.
 */
export class AuthController {
  constructor(
    private readonly loginUseCase: LoginUseCase,
    private readonly getStudentUseCase: GetStudentUseCase
  ) {}

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
      // @ts-ignore - sub added by authMiddleware (it is the student database ID)
      const studentId = req.user?.sub;
      if (!studentId) {
        return res.status(401).json({ success: false, message: "Unauthorized" });
      }

      const student = await this.getStudentUseCase.execute(studentId);
      
      // Split fullName into firstName and lastName for Android app compatibility
      const nameParts = student.fullName.split(" ");
      const firstName = nameParts[0] || "";
      const lastName = nameParts.slice(1).join(" ") || "";

      res.status(200).json({ 
        success: true, 
        data: { 
          id: student.id,
          studentId: student.studentId,
          firstName,
          lastName,
          email: student.email,
          program: "BS Computer Science", // Placeholder for now, can be fetched from enrollment if needed
          yearLevel: 3 // Placeholder
        } 
      });
    } catch (err) {
      next(err);
    }
  };
}
