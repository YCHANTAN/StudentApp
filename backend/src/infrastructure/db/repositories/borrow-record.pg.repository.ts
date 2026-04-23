import { eq, and, isNull } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { BorrowRecord } from '@/core/entities/borrow-record.entity';
import { borrowRecords } from '../schema/borrow-record.schema.js';
import { books } from '../schema/book.schema.js';

export class BorrowRecordPgRepository implements BorrowRecordRepository {
  constructor(private readonly db: NodePgDatabase<Record<string, never>>) {}

  async findById(id: string): Promise<BorrowRecord | null> {
    const [row] = await this.db.select().from(borrowRecords).where(eq(borrowRecords.id, id));
    return row ?? null;
  }

  async findActiveRecord(bookId: string, userId: string): Promise<BorrowRecord | null> {
    const [row] = await this.db.select()
      .from(borrowRecords)
      .where(
        and(
          eq(borrowRecords.bookId, bookId),
          eq(borrowRecords.userId, userId),
          isNull(borrowRecords.returnedAt) // This is the crucial check!
        )
      );
    return row ?? null;
  }

  async findHistoryByUserId(userId: string): Promise<BorrowRecord[]> {
    const rows = await this.db
      .select({
        id: borrowRecords.id,
        bookId: borrowRecords.bookId,
        userId: borrowRecords.userId,
        borrowedAt: borrowRecords.borrowedAt,
        dueDate: borrowRecords.dueDate,
        returnedAt: borrowRecords.returnedAt,
        bookTitle: books.title,
        bookAuthor: books.author,
      })
      .from(borrowRecords)
      .innerJoin(books, eq(borrowRecords.bookId, books.id))
      .where(eq(borrowRecords.userId, userId));

    return rows;
  }

  async save(record: BorrowRecord): Promise<BorrowRecord> {
    const [row] = await this.db.insert(borrowRecords).values(record).returning();
    if (!row) throw new Error('Failed to persist borrow record');
    return row;
  }

  async update(id: string, data: Partial<BorrowRecord>): Promise<BorrowRecord> {
    const [row] = await this.db.update(borrowRecords).set(data).where(eq(borrowRecords.id, id)).returning();
    if (!row) throw new Error('Failed to update borrow record');
    return row;
  }
}