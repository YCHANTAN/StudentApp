import type { Request, Response, NextFunction } from 'express';
import { GetLibraryBooksQueryDto, UpdateLibraryBookDto } from '@/application/dtos/library-book.dto';
import { PaginationDto } from '@/application/dtos/pagination.dto';
import type { GetLibraryBooksUseCase } from '@/application/use-cases/library-book/get-library-books.use-case';
import type { UpdateLibraryBookUseCase } from '@/application/use-cases/library-book/update-library-book.use-case';
import { ok } from '@/presentation/lib/response.helper';

export class LibraryBookController {
  constructor(
    private readonly getLibraryBooksUseCase: GetLibraryBooksUseCase,
    private readonly updateLibraryBookUseCase: UpdateLibraryBookUseCase
  ) {}

  getLibraryBooks = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const pagination = PaginationDto.parse({
        page: req.query.page,
        limit: req.query.limit,
      });
      const filter = GetLibraryBooksQueryDto.parse(req.query);

      const { data, total } = await this.getLibraryBooksUseCase.execute({
        ...pagination,
        tab: filter.tab,
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
      const book = await this.updateLibraryBookUseCase.execute(req.params.id, req.body);
      ok(res, book);
    } catch (err) {
      next(err);
    }
  };
}
