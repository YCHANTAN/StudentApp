import { Router } from 'express';
import { UpdateLibraryBookDto } from '@/application/dtos/library-book.dto';
import { BorrowBookDto, ReturnBookDto } from '@/application/dtos/borrow-record.dto';
import { libraryBookController } from '@/container';
import { validate } from '@/presentation/middleware/validate.middleware';

export const libraryBookRouter = Router();

libraryBookRouter.get('/', libraryBookController.getLibraryBooks);
libraryBookRouter.get('/history/:userId', libraryBookController.getBorrowHistory);
libraryBookRouter.put('/:id', validate(UpdateLibraryBookDto), libraryBookController.updateLibraryBook);
libraryBookRouter.post('/:id/borrow', validate(BorrowBookDto), libraryBookController.borrowBook);
libraryBookRouter.post('/:id/return', validate(ReturnBookDto), libraryBookController.returnBook);
