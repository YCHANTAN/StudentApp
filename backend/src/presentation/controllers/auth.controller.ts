import type { NextFunction, Request, Response } from "express";

import type { LoginUseCase } from "@/application/use-cases/auth/login.use-case";
import type { GetStudentUseCase } from "@/application/use-cases/student/get-student.use-case";
import type { GetStudentProfileUseCase } from "@/application/use-cases/student-profile/get-student-profile.use-case";

/**
 * Handles HTTP transport concerns for authentication endpoints.
 */
export class AuthController {
  constructor(
    private readonly loginUseCase: LoginUseCase,
    private readonly getStudentUseCase: GetStudentUseCase,
    private readonly getStudentProfileUseCase: GetStudentProfileUseCase
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
      const profile = await this.getStudentProfileUseCase.execute(studentId);
      
      // Split fullName into firstName and lastName for Android app compatibility
      const nameParts = profile.fullName.split(" ");
      const firstName = nameParts[0] || "";
      const lastName = nameParts.slice(1).join(" ") || "";

      // Extract Year Level from programSummary (e.g., "BS Computer Science • Year 2")
      const yearMatch = profile.programSummary.match(/Year\s+(\d+)/i);
      const yearLevel = yearMatch ? parseInt(yearMatch[1], 10) : 1;

      // Extract Program from programSummary (e.g., "BS Computer Science • Year 2")
      const program = profile.programSummary.split("•")[0]?.trim() || "N/A";

      res.status(200).json({ 
        success: true, 
        data: { 
          id: student.id,
          studentId: student.studentId,
          firstName,
          lastName,
          email: student.email,
          program: program,
          yearLevel: yearLevel,
          phoneNumber: profile.phoneNumber,
          accountLabel: profile.accountLabel,
          programSummary: profile.programSummary,
          emergencyContact: {
            name: profile.emergencyContactName,
            relationship: profile.emergencyContactRelationship,
            phoneNumber: profile.emergencyContactPhoneNumber
          },
          notifications: {
            email: profile.emailNotifications,
            sms: profile.smsNotifications,
            system: profile.systemAlerts
          }
        } 
      });
    } catch (err) {
      next(err);
    }
  };
}
