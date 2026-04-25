import type { Request, Response, NextFunction } from 'express';
import { GetProgramsQueryDto } from '@/application/dtos/program.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetProgramsUseCase } from '@/application/use-cases/program/get-programs.use-case';
import type { GetProgramUseCase } from '@/application/use-cases/program/get-program.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class ProgramController {
  constructor(
    private readonly getProgramsUseCase: GetProgramsUseCase,
    private readonly getProgramUseCase: GetProgramUseCase
  ) {}

  getPrograms = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetProgramsQueryDto.parse(req.query);

      const { data, total } = await this.getProgramsUseCase.execute({
        ...pagination,
        ...(filter.category !== undefined && { category: filter.category }),
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

  getProgram = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const id = req.params.id as string;
      const program = await this.getProgramUseCase.execute(id);
      ok(res, program);
    } catch (err) {
      next(err);
    }
  };
}
