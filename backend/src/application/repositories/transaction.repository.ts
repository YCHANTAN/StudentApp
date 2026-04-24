import type { Transaction } from '@/core/entities/transaction.entity';

export interface TransactionRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { studentId?: string }): Promise<{ data: Transaction[]; total: number }>;
  findById(id: string): Promise<Transaction | null>;
  save(transaction: Transaction): Promise<Transaction>;
}
