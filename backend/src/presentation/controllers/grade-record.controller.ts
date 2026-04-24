import type { Request, Response, NextFunction } from 'express';
import { GetGradeRecordsQueryDto } from '@/application/dtos/grade-record.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetGradeRecordsUseCase } from '@/application/use-cases/grade-record/get-grade-records.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class GradeRecordController {
  constructor(private readonly getGradeRecordsUseCase: GetGradeRecordsUseCase) {}

  getGradeRecords = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetGradeRecordsQueryDto.parse(req.query);

      const { data, total } = await this.getGradeRecordsUseCase.execute({
        ...pagination,
        studentId: filter.studentId,
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
}
