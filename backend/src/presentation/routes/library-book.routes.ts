import { Router } from 'express';
import { UpdateLibraryBookDto } from '@/application/dtos/library-book.dto';
import { BorrowBookDto, ReturnBookDto } from '@/application/dtos/borrow-record.dto';
import { libraryBookController } from '@/container';
import { validate } from '@/presentation/middleware/validate.middleware';
import { authMiddleware } from '@/presentation/middleware/auth.middleware';

export const libraryBookRouter = Router();

libraryBookRouter.get('/', authMiddleware, libraryBookController.getLibraryBooks);
libraryBookRouter.get('/history/:userId', authMiddleware, libraryBookController.getBorrowHistory);
libraryBookRouter.put('/:id', authMiddleware, validate(UpdateLibraryBookDto), libraryBookController.updateLibraryBook);
libraryBookRouter.post('/:id/borrow', authMiddleware, validate(BorrowBookDto), libraryBookController.borrowBook);
libraryBookRouter.post('/:id/return', authMiddleware, validate(ReturnBookDto), libraryBookController.returnBook);
