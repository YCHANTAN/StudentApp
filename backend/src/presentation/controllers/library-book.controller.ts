import type { Request, Response, NextFunction } from 'express';
import { GetLibraryBooksQueryDto, UpdateLibraryBookDto } from '@/application/dtos/library-book.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetLibraryBooksUseCase } from '@/application/use-cases/library-book/get-library-books.use-case';
import type { UpdateLibraryBookUseCase } from '@/application/use-cases/library-book/update-library-book.use-case';
import type { BorrowBookUseCase } from '@/application/use-cases/library-book/borrow-book.use-case';
import type { ReturnBookUseCase } from '@/application/use-cases/library-book/return-book.use-case';
import type { GetBorrowHistoryUseCase } from '@/application/use-cases/library-book/get-borrow-history.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class LibraryBookController {
  constructor(
    private readonly getLibraryBooksUseCase: GetLibraryBooksUseCase,
    private readonly updateLibraryBookUseCase: UpdateLibraryBookUseCase,
    private readonly borrowBookUseCase: BorrowBookUseCase,
    private readonly returnBookUseCase: ReturnBookUseCase,
    private readonly getBorrowHistoryUseCase: GetBorrowHistoryUseCase,
  ) {}

  private getIdOrFail(req: Request, paramName: string = 'id'): string {
    const val = req.params[paramName];
    if (!val || typeof val !== 'string') {
      throw new Error(`Invalid ${paramName} parameter`);
    }
    return val;
  }

  getLibraryBooks = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetLibraryBooksQueryDto.parse(req.query);
      const userId = req.user?.sub;

      const { data, total } = await this.getLibraryBooksUseCase.execute({
        ...pagination,
        ...(filter.tab !== undefined && { tab: filter.tab }),
        userId,
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

  updateLibraryBook = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const book = await this.updateLibraryBookUseCase.execute(this.getIdOrFail(req), req.body);
      ok(res, book);
    } catch (err) {
      next(err);
    }
  };

  borrowBook = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const record = await this.borrowBookUseCase.execute(this.getIdOrFail(req), req.body);
      ok(res, record);
    } catch (err) {
      next(err);
    }
  };

  returnBook = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const record = await this.returnBookUseCase.execute(this.getIdOrFail(req), req.body);
      ok(res, record);
    } catch (err) {
      next(err);
    }
  };

  getBorrowHistory = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const history = await this.getBorrowHistoryUseCase.execute(this.getIdOrFail(req, 'userId'));
      ok(res, history);
    } catch (err) {
      next(err);
    }
  };
}
