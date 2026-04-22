import type { NextFunction, Request, Response } from "express";

import type { CreateStudentUseCase } from "@/application/use-cases/student/create-student.use-case";
import type { DeleteStudentUseCase } from "@/application/use-cases/student/delete-student.use-case";
import type { GetStudentUseCase } from "@/application/use-cases/student/get-student.use-case";
import type { UpdateStudentUseCase } from "@/application/use-cases/student/update-student.use-case";

export class StudentController {
  constructor(
    private readonly createStudent: CreateStudentUseCase,
    private readonly getStudent: GetStudentUseCase,
    private readonly updateStudent: UpdateStudentUseCase,
    private readonly deleteStudent: DeleteStudentUseCase,
  ) {}

  private getIdOrFail(req: Request): string {
    const { id } = req.params;
    if (!id || Array.isArray(id)) {
      throw new Error("Invalid student id parameter");
    }
    return id;
  }

  create = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const student = await this.createStudent.execute(req.body);
      res.status(201).json({ success: true, data: student });
    } catch (err) {
      next(err);
    }
  };

  findById = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const student = await this.getStudent.execute(this.getIdOrFail(req));
      res.status(200).json({ success: true, data: student });
    } catch (err) {
      next(err);
    }
  };

  update = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const student = await this.updateStudent.execute(this.getIdOrFail(req), req.body);
      res.status(200).json({ success: true, data: student });
    } catch (err) {
      next(err);
    }
  };

  remove = async (req: Request, res: Response, next: NextFunction) => {
    try {
      await this.deleteStudent.execute(this.getIdOrFail(req));
      res.status(200).json({ success: true, data: null });
    } catch (err) {
      next(err);
    }
  };
}
