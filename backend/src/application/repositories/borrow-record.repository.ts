import type { BorrowRecord } from '@/core/entities/borrow-record.entity';

export interface BorrowRecordRepository {
  findById(id: string): Promise<BorrowRecord | null>;
  findActiveRecord(bookId: string, studentId: string): Promise<BorrowRecord | null>; 
  findHistoryByUserId(studentId: string): Promise<BorrowRecord[]>;
  save(record: BorrowRecord): Promise<BorrowRecord>;
  update(id: string, data: Partial<BorrowRecord>): Promise<BorrowRecord>;
}