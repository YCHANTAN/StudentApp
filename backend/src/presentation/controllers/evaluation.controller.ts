import type { Request, Response, NextFunction } from 'express';
import { CreateEvaluationDto } from '@/application/dtos/evaluation.dto';
import type { SubmitEvaluationUseCase } from '@/application/use-cases/evaluation/submit-evaluation.use-case';
import type { GetStudentEvaluationsUseCase } from '@/application/use-cases/evaluation/get-student-evaluations.use-case';
import { ok, created } from '@/presentation/lib/response.helper';

export class EvaluationController {
  constructor(
    private readonly submitEvaluationUseCase: SubmitEvaluationUseCase,
    private readonly getStudentEvaluationsUseCase: GetStudentEvaluationsUseCase
  ) {}

  submitEvaluation = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const dto = CreateEvaluationDto.parse(req.body);
      const result = await this.submitEvaluationUseCase.execute(dto);
      created(res, result);
    } catch (err) {
      next(err);
    }
  };

  getStudentEvaluations = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = req.params.studentId;
      if (typeof studentId !== 'string') {
        return res.status(400).json({ success: false, message: 'Invalid Student ID' });
      }
      const result = await this.getStudentEvaluationsUseCase.execute(studentId);
      ok(res, result);
    } catch (err) {
      next(err);
    }
  };
}
