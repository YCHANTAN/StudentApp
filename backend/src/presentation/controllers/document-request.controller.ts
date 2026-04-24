import type { Request, Response, NextFunction } from 'express';
import { GetDocumentRequestsQueryDto, CreateDocumentRequestDto } from '@/application/dtos/document-request.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetDocumentRequestsUseCase } from '@/application/use-cases/document-request/get-document-requests.use-case';
import type { GetDocumentRequestUseCase } from '@/application/use-cases/document-request/get-document-request.use-case';
import type { CreateDocumentRequestUseCase } from '@/application/use-cases/document-request/create-document-request.use-case';
import { ok, created } from '@/presentation/lib/response.helper';

export class DocumentRequestController {
  constructor(
    private readonly getDocumentRequestsUseCase: GetDocumentRequestsUseCase,
    private readonly getDocumentRequestUseCase: GetDocumentRequestUseCase,
    private readonly createDocumentRequestUseCase: CreateDocumentRequestUseCase
  ) {}

  getDocumentRequests = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetDocumentRequestsQueryDto.parse(req.query);

      const { data, total } = await this.getDocumentRequestsUseCase.execute({
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

  getDocumentRequest = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const request = await this.getDocumentRequestUseCase.execute(req.params.id);
      ok(res, request);
    } catch (err) {
      next(err);
    }
  };

  createDocumentRequest = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const request = await this.createDocumentRequestUseCase.execute(req.body);
      created(res, request);
    } catch (err) {
      next(err);
    }
  };
}
