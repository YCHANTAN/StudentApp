// src/presentation/controllers/subject.controller.ts
import type { Request, Response, NextFunction } from 'express';
import type { CreateSubjectUseCase } from '@/application/use-cases/subject/create-subject.use-case';
import { created } from '@/presentation/lib/response.helper';

export class SubjectController {
  constructor(private readonly createSubjectUseCase: CreateSubjectUseCase) {}

  create = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const subject = await this.createSubjectUseCase.execute(req.body);
      created(res, subject);
    } catch (err) {
      next(err);
    }
  };
}
