import type { NextFunction, Request, Response } from "express";
import type { GetStudentProfileUseCase } from "@/application/use-cases/student-profile/get-student-profile.use-case";
import type { UpdateStudentProfileUseCase } from "@/application/use-cases/student-profile/update-student-profile.use-case";
import { ok } from "@/presentation/lib/response.helper";

export class StudentProfileController {
  constructor(
    private readonly getStudentProfileUseCase: GetStudentProfileUseCase,
    private readonly updateStudentProfileUseCase: UpdateStudentProfileUseCase,
  ) {}

  getStudentProfile = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const profile = await this.getStudentProfileUseCase.execute(req.params.id);
      ok(res, profile);
    } catch (err) {
      next(err);
    }
  };

  updateStudentProfile = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const profile = await this.updateStudentProfileUseCase.execute(req.params.id, req.body);
      ok(res, profile);
    } catch (err) {
      next(err);
    }
  };
}
