import type { Request, Response, NextFunction } from 'express';
import { GetTransactionsQueryDto } from '@/application/dtos/transaction.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetTransactionsUseCase } from '@/application/use-cases/transaction/get-transactions.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class TransactionController {
  constructor(private readonly getTransactionsUseCase: GetTransactionsUseCase) {}

  getTransactions = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetTransactionsQueryDto.parse(req.query);

      const { data, total } = await this.getTransactionsUseCase.execute({
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
