import { eq, and, isNull } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { BorrowRecord } from '@/core/entities/borrow-record.entity';
import { borrowRecords } from '../schema/borrow-record.schema';
import { libraryBooks } from '../schema/library-book.schema';

export class BorrowRecordPgRepository implements BorrowRecordRepository {
  constructor(private readonly db: NodePgDatabase) {}

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
          isNull(borrowRecords.returnedAt)
        )
      );
    return row ?? null;
  }

  async findHistoryByUserId(userId: string): Promise<BorrowRecord[]> {
    return this.db.select().from(borrowRecords).where(eq(borrowRecords.userId, userId));
  }

  async save(record: BorrowRecord): Promise<BorrowRecord> {
    const [row] = await this.db.insert(borrowRecords).values(record).returning();
    if (!row) throw new Error('Failed to save borrow record');
    return row;
  }

  async update(id: string, data: Partial<BorrowRecord>): Promise<BorrowRecord> {
    const [row] = await this.db.update(borrowRecords).set(data).where(eq(borrowRecords.id, id)).returning();
    if (!row) throw new Error('Failed to update borrow record');
    return row;
  }
}
