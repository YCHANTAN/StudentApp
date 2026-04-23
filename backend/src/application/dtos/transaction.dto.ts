// src/application/dtos/transaction.dto.ts
import { z } from 'zod';

export const CreateTransactionDto = z.object({
  studentId: z.string().min(1, "A valid Student ID is required"),
  
  // FIX: Just pass the array. Let Zod handle the default error message!
  type: z.enum(['CHARGE', 'PAYMENT']),
  
  amount: z.coerce.number().int().positive("Amount must be a positive number"),
  
  // FIX: Just pass the array and chain the default value.
  method: z.enum(['CASH', 'GCASH', 'CREDIT_CARD', 'BANK_TRANSFER', 'NONE']).default('NONE'),
  
  description: z.string().min(3, "Please provide a valid description"),
});

export type CreateTransactionInput = z.infer<typeof CreateTransactionDto>;