import { eq, count, desc } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { Transaction } from '@/core/entities/transaction.entity';
import { transactions } from '../schema/transaction.schema';

export class TransactionPgRepository implements TransactionRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { studentId?: string }
  ): Promise<{ data: Transaction[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.studentId ? eq(transactions.studentId, filter.studentId) : undefined;

    const data = await this.db
      .select()
      .from(transactions)
      .where(whereClause)
      .limit(limit)
      .offset(offset)
      .orderBy(desc(transactions.date));

    const [totalResult] = await this.db
      .select({ value: count() })
      .from(transactions)
      .where(whereClause);

    return {
      data: data.map(this.mapToEntity),
      total: Number(totalResult.value),
    };
  }

  async findById(id: string): Promise<Transaction | null> {
    const [row] = await this.db.select().from(transactions).where(eq(transactions.id, id));
    return row ? this.mapToEntity(row) : null;
  }

  async save(transaction: Transaction): Promise<Transaction> {
    const [row] = await this.db.insert(transactions).values(transaction).returning();
    if (!row) throw new Error('Failed to save transaction');
    return this.mapToEntity(row);
  }

  private mapToEntity(row: any): Transaction {
    return {
      id: row.id,
      studentId: row.studentId,
      title: row.title,
      date: row.date,
      amount: row.amount,
      isPaid: row.isPaid,
      createdAt: row.createdAt,
    };
  }
}
