import { eq, desc } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { Transaction, TransactionStatus } from '@/core/entities/transaction.entity';
import { transactions } from '../schema/transaction.schema.js';

export class TransactionPgRepository implements TransactionRepository {
  constructor(private readonly db: NodePgDatabase<Record<string, never>>) {}

  async save(transaction: Transaction): Promise<Transaction> {
    const [row] = await this.db.insert(transactions).values(transaction).returning();
    if (!row) throw new Error('Failed to create transaction');
    return row as Transaction;
  }

  async findById(id: string): Promise<Transaction | null> {
    const [row] = await this.db.select().from(transactions).where(eq(transactions.id, id));
    return (row as Transaction) ?? null;
  }

  async findByStudentId(studentId: string): Promise<Transaction[]> {
    const rows = await this.db
      .select()
      .from(transactions)
      .where(eq(transactions.studentId, studentId))
      .orderBy(desc(transactions.createdAt));
    return rows as Transaction[];
  }

  async updateStatus(id: string, status: TransactionStatus): Promise<Transaction | null> {
    const [row] = await this.db
      .update(transactions)
      .set({ status })
      .where(eq(transactions.id, id))
      .returning();
    return (row as Transaction) ?? null;
  }
}