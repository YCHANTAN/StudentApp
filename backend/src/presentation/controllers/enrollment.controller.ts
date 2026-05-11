import type { Request, Response, NextFunction } from 'express';
import { GetEnrollmentsQueryDto, CreateEnrollmentDto, UpdateEnrollmentDto } from '@/application/dtos/enrollment.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetEnrollmentsUseCase } from '@/application/use-cases/enrollment/get-enrollments.use-case';
import type { CreateEnrollmentUseCase } from '@/application/use-cases/enrollment/create-enrollment.use-case';
import type { UpdateEnrollmentUseCase } from '@/application/use-cases/enrollment/update-enrollment.use-case';
import type { DeleteEnrollmentUseCase } from '@/application/use-cases/enrollment/delete-enrollment.use-case';
import type { GetStudyLoadPdfUseCase } from '@/application/use-cases/enrollment/get-study-load-pdf.use-case';
import type { GetStudyLoadUseCase } from '@/application/use-cases/enrollment/get-study-load.use-case';
import { ok, created } from '@/presentation/lib/response.helper';

export class EnrollmentController {
  constructor(
    private readonly getEnrollmentsUseCase: GetEnrollmentsUseCase,
    private readonly createEnrollmentUseCase: CreateEnrollmentUseCase,
    private readonly updateEnrollmentUseCase: UpdateEnrollmentUseCase,
    private readonly deleteEnrollmentUseCase: DeleteEnrollmentUseCase,
    private readonly getStudyLoadPdfUseCase: GetStudyLoadPdfUseCase,
    private readonly getStudyLoadUseCase: GetStudyLoadUseCase
  ) {}

  getEnrollments = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetEnrollmentsQueryDto.parse(req.query);

      const { data, total } = await this.getEnrollmentsUseCase.execute({
        ...pagination,
        ...(filter.studentId !== undefined && { studentId: filter.studentId }),
      });

      ok(res, data, {
        total,
        page: pagination.page,
        limit: pagination.limit,
        totalPages: Math.ceil(total / pagination.limit),
      });
    } catch (err) {
      next(err);
    }
  };

  getStudyLoad = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = req.params.studentId as string;
      const studyLoad = await this.getStudyLoadUseCase.execute(studentId);
      ok(res, studyLoad);
    } catch (err) {
      next(err);
    }
  };

  getStudyLoadPdf = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = req.params.studentId as string;
      const pdfBuffer = await this.getStudyLoadPdfUseCase.execute(studentId);
      
      res.setHeader('Content-Type', 'application/pdf');
      res.setHeader('Content-Disposition', `attachment; filename=study_load_${studentId}.pdf`);
      res.send(pdfBuffer);
    } catch (err) {
      next(err);
    }
  };

  create = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const enrollment = await this.createEnrollmentUseCase.execute(req.body);
      created(res, enrollment);
    } catch (err) {
      next(err);
    }
  };

  update = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = req.params.id as string;
      const enrollment = await this.updateEnrollmentUseCase.execute(id, req.body);
      ok(res, enrollment);
    } catch (err) {
      next(err);
    }
  };

  delete = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = req.params.id as string;
      await this.deleteEnrollmentUseCase.execute(id);
      ok(res, null);
    } catch (err) {
      next(err);
    }
  };
}
