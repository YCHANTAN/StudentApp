import type { Request, Response, NextFunction } from 'express';
import { GetComplaintsQueryDto, CreateComplaintDto } from '@/application/dtos/complaint.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetComplaintsUseCase } from '@/application/use-cases/complaint/get-complaints.use-case';
import type { CreateComplaintUseCase } from '@/application/use-cases/complaint/create-complaint.use-case';
import { ok, created } from '@/presentation/lib/response.helper';

export class ComplaintController {
  constructor(
    private readonly getComplaintsUseCase: GetComplaintsUseCase,
    private readonly createComplaintUseCase: CreateComplaintUseCase
  ) {}

  getComplaints = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetComplaintsQueryDto.parse(req.query);

      const { data, total } = await this.getComplaintsUseCase.execute({
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

  createComplaint = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const complaint = await this.createComplaintUseCase.execute(req.body);
      created(res, complaint);
    } catch (err) {
      next(err);
    }
  };
}
