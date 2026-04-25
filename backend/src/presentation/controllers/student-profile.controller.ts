import type { NextFunction, Request, Response } from "express";
import type { GetStudentProfileUseCase } from "@/application/use-cases/student-profile/get-student-profile.use-case";
import type { GetStudentProfilesUseCase } from "@/application/use-cases/student-profile/get-student-profiles.use-case";
import type { UpdateStudentProfileUseCase } from "@/application/use-cases/student-profile/update-student-profile.use-case";
import { ok } from "@/presentation/lib/response.helper";

export class StudentProfileController {
  constructor(
    private readonly getStudentProfileUseCase: GetStudentProfileUseCase,
    private readonly getStudentProfilesUseCase: GetStudentProfilesUseCase,
    private readonly updateStudentProfileUseCase: UpdateStudentProfileUseCase,
  ) {}

  private getIdOrFail(req: Request): string {
    const { id } = req.params;
    if (!id || typeof id !== "string") {
      throw new Error("Invalid student id parameter");
    }
    return id;
  }

  listStudents = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const profiles = await this.getStudentProfilesUseCase.execute();
      ok(res, profiles);
    } catch (err) {
      next(err);
    }
  };

  getStudentProfile = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const profile = await this.getStudentProfileUseCase.execute(this.getIdOrFail(req));
      ok(res, profile);
    } catch (err) {
      next(err);
    }
  };

  updateStudentProfile = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const profile = await this.updateStudentProfileUseCase.execute(this.getIdOrFail(req), req.body);
      ok(res, profile);
    } catch (err) {
      next(err);
    }
  };
}
