import { Router } from 'express';
import { validate } from '../middleware/validate.middleware.js';
import { BorrowBookDto, ReturnBookDto } from '../../application/dtos/borrow-record.dto.js';
import { bookController } from '../../container.js';

import { requireAuth } from '../middleware/auth.middleware.js'; 

export const bookRouter = Router();

bookRouter.use(requireAuth);

bookRouter.post('/:id/borrow', validate(BorrowBookDto), bookController.borrowBook);
bookRouter.post('/:id/return', validate(ReturnBookDto), bookController.returnBook);
bookRouter.get('/borrow-records/:studentId', bookController.getStudentHistory);