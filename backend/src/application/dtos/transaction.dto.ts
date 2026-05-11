import { z } from 'zod';

export const CreateTransactionDto = z.object({
  studentId: z.string(),
  title: z.string(),
  type: z.enum(['PAYMENT', 'FEE', 'REFUND']),
  amount: z.string(),
  method: z.string(),
  description: z.string().optional(),
  date: z.string().datetime().optional(),
  isPaid: z.boolean().optional(),
});

export const UpdateTransactionDto = CreateTransactionDto.partial();

export const TransactionResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  title: z.string(),
  type: z.enum(['PAYMENT', 'FEE', 'REFUND']),
  amount: z.string(),
  method: z.string(),
  status: z.enum(['PENDING', 'COMPLETED', 'FAILED']),
  referenceId: z.string(),
  description: z.string().nullable(),
  date: z.string().datetime(),
  isPaid: z.boolean(),
  createdAt: z.string().datetime(),
});

export type CreateTransactionInput = z.infer<typeof CreateTransactionDto>;
export type UpdateTransactionInput = z.infer<typeof UpdateTransactionDto>;

export const GetTransactionsQueryDto = z.object({
  studentId: z.string().optional(),
});
