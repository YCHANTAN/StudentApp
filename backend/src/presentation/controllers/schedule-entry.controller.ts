import type { Request, Response, NextFunction } from 'express';
import { GetScheduleEntriesQueryDto } from '@/application/dtos/schedule-entry.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetScheduleEntriesUseCase } from '@/application/use-cases/schedule-entry/get-schedule-entries.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class ScheduleEntryController {
  constructor(private readonly getScheduleEntriesUseCase: GetScheduleEntriesUseCase) {}

  getScheduleEntries = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetScheduleEntriesQueryDto.parse(req.query);

      const { data, total } = await this.getScheduleEntriesUseCase.execute({
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
}
