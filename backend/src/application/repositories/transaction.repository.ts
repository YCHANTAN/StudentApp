import type { Transaction, TransactionStatus } from '@/core/entities/transaction.entity';

export interface TransactionRepository {
  save(transaction: Transaction): Promise<Transaction>;
  findById(id: string): Promise<Transaction | null>;
  findByStudentId(studentId: string): Promise<Transaction[]>;
  updateStatus(id: string, status: TransactionStatus): Promise<Transaction | null>;
}