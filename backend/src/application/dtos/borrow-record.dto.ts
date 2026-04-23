import { z } from 'zod';

export const BorrowBookDto = z.object({
  userId: z.string().min(1, "A valid User ID is required to borrow a book"),
});

export const ReturnBookDto = z.object({
  userId: z.string().min(1, "A valid User ID is required to return a book"),
});

export type BorrowBookInput = z.infer<typeof BorrowBookDto>;
export type ReturnBookInput = z.infer<typeof ReturnBookDto>;