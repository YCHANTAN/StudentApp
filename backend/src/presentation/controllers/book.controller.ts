import type { Request, Response, NextFunction } from 'express';
import { ok, created } from '../lib/response.helper.js'; 
import type { BorrowBookUseCase } from '../../application/use-cases/book/borrow-book.use-case.js';
import type { ReturnBookUseCase } from '../../application/use-cases/book/return-book.use-case.js';
import type { BorrowHistoryUseCase } from '@/application/use-cases/book/borrow-history.use-case';

export interface AuthRequest<P = any> extends Request<P> {
  user?: {
    studentId: string;
  };
}

export class BookController {
  constructor(
    private readonly borrowBookUseCase: BorrowBookUseCase,
    private readonly returnBookUseCase: ReturnBookUseCase,
    private readonly getHistoryUseCase: BorrowHistoryUseCase
  ) {}

  borrowBook = async (req: AuthRequest<{ id: string }>, res: Response, next: NextFunction) => {
    try {
      const bookId = req.params.id; 
      const studentId = req.user?.studentId;

      if (!studentId) {
        res.status(401).json({ success: false, error: 'Unauthorized' });
        return;
      }

      const record = await this.borrowBookUseCase.execute(bookId, { studentId });
      
      created(res, record);
    } catch (err) {
      next(err); 
    }
  };

  returnBook = async (req: AuthRequest<{ id: string }>, res: Response, next: NextFunction) => {
    try {
      const bookId = req.params.id;
      const studentId = req.user?.studentId;

      if (!studentId) {
        res.status(401).json({ success: false, error: 'Unauthorized' });
        return;
      }

      const record = await this.returnBookUseCase.execute(bookId, { studentId });
      
      ok(res, record);
    } catch (err) {
      next(err);
    }
  };

  getStudentHistory = async (req: AuthRequest<{ studentId: string }>, res: Response, next: NextFunction) => {
    try {
      const studentId = req.params.studentId;
      const result = await this.getHistoryUseCase.execute(studentId);
      ok(res, result);
    } catch (err) {
      next(err); 
    }
  };
}