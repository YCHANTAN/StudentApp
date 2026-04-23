import { z } from 'zod';

export const BorrowBookDto = z.object({});
export const ReturnBookDto = z.object({});

export type BorrowBookInput = {
  studentId: string;
};

export type ReturnBookInput = {
  studentId: string;
};