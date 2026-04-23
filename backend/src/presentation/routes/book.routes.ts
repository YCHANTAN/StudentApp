import { Router } from 'express';
import { validate } from '../middleware/validate.middleware.js';
import { BorrowBookDto, ReturnBookDto } from '../../application/dtos/borrow-record.dto.js';

import { bookController } from '../../container.js'

export const bookRouter = Router();

// POST /api/v1/books/:id/borrow
bookRouter.post(
  '/:id/borrow', 
  validate(BorrowBookDto), 
  bookController.borrowBook
);

// POST /api/v1/books/:id/return
bookRouter.post(
  '/:id/return', 
  validate(ReturnBookDto), 
  bookController.returnBook
);

// GET /api/v1/books/history/:userId
bookRouter.get(
  '/history/:userId',
  bookController.getBorrowHistory
);