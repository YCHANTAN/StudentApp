import type { Request, Response, NextFunction } from 'express';
import { ok, created } from '../lib/response.helper.js';
import type { BorrowBookUseCase } from '../../application/use-cases/book/borrow-book.use-case.js';
import type { ReturnBookUseCase } from '../../application/use-cases/book/return-book.use-case.js';
import type { GetBorrowHistoryUseCase } from '../../application/use-cases/book/get-borrow-history.use-case.js';

export class BookController {
  constructor(
    private readonly borrowBookUseCase: BorrowBookUseCase,
    private readonly returnBookUseCase: ReturnBookUseCase,
    private readonly getBorrowHistoryUseCase: GetBorrowHistoryUseCase,
  ) {}

  getBorrowHistory = async (req: Request<{ userId: string }>, res: Response, next: NextFunction) => {
    try {
      const { userId } = req.params;
      const history = await this.getBorrowHistoryUseCase.execute(userId);
      ok(res, history);
    } catch (err) {
      next(err);
    }
  };

  borrowBook = async (req: Request<{ id: string }>, res: Response, next: NextFunction) => {
    try {
      const record = await this.borrowBookUseCase.execute(req.params.id, req.body);
      
      // Use the API standard helper for 201 Created
      created(res, record);
    } catch (err) {
      next(err); 
    }
  };

  returnBook = async (req: Request<{ id: string }>, res: Response, next: NextFunction) => {
    try {
      const record = await this.returnBookUseCase.execute(req.params.id, req.body);
      
      // Use the API standard helper for 200 OK
      ok(res, record);
    } catch (err) {
      next(err);
    }
  };
}