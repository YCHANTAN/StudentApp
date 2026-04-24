import type { Request, Response, NextFunction } from 'express';
import { GetCoursesQueryDto } from '@/application/dtos/course.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetCoursesUseCase } from '@/application/use-cases/course/get-courses.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class CourseController {
  constructor(private readonly getCoursesUseCase: GetCoursesUseCase) {}

  getCourses = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetCoursesQueryDto.parse(req.query);

      const { data, total } = await this.getCoursesUseCase.execute({
        ...pagination,
        programId: filter.programId,
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
