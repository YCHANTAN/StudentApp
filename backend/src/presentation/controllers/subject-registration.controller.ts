import type { Request, Response } from 'express';
import type { GetStudentSubjectsUseCase } from '@/application/use-cases/registration/get-student-subjects.use-case';

export class SubjectRegistrationController {
  constructor(private readonly getStudentSubjectsUseCase: GetStudentSubjectsUseCase) {}

  getEnrolledSubjects = async (req: Request<{ id: string }>, res: Response) => {
    try {
      const data = await this.getStudentSubjectsUseCase.execute(req.params.id, 'Enrolled');
      res.status(200).json({ success: true, term: "Spring Semester 2024", data });
    } catch (error: any) {
      res.status(400).json({ success: false, error: error.message });
    }
  };

  getCompletedSubjects = async (req: Request<{ id: string }>, res: Response) => {
    try {
      const data = await this.getStudentSubjectsUseCase.execute(req.params.id, 'Completed');
      res.status(200).json({ success: true, term: "Fall Semester 2023", data });
    } catch (error: any) {
      res.status(400).json({ success: false, error: error.message });
    }
  };

  getWaitlistedSubjects = async (req: Request<{ id: string }>, res: Response) => {
    try {
      const data = await this.getStudentSubjectsUseCase.execute(req.params.id, 'Waitlisted');
      res.status(200).json({ success: true, term: "Spring Semester 2024", data });
    } catch (error: any) {
      res.status(400).json({ success: false, error: error.message });
    }
  };
}