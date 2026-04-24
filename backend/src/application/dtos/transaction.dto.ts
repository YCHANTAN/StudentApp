import { z } from 'zod';

export const CreateTransactionDto = z.object({
  studentId: z.string(),
  title: z.string(),
  date: z.string().datetime(),
  amount: z.string(),
  isPaid: z.boolean(),
});

export const UpdateTransactionDto = CreateTransactionDto.partial();

export const TransactionResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  title: z.string(),
  date: z.string().datetime(),
  amount: z.string(),
  isPaid: z.boolean(),
  createdAt: z.string().datetime(),
});

export type CreateTransactionInput = z.infer<typeof CreateTransactionDto>;
export type UpdateTransactionInput = z.infer<typeof UpdateTransactionDto>;

export const GetTransactionsQueryDto = z.object({
  studentId: z.string().optional(),
});
