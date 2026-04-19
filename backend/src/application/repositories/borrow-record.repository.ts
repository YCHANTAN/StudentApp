import type { BorrowRecord } from '@/core/entities/borrow-record.entity';

export interface BorrowRecordRepository {
  findById(id: string): Promise<BorrowRecord | null>;
  
  // Checks if a specific user currently has a specific book checked out
  findActiveRecord(bookId: string, userId: string): Promise<BorrowRecord | null>; 
  
  // Gets all the books a user has ever borrowed (for their history)
  findHistoryByUserId(userId: string): Promise<BorrowRecord[]>;
  
  save(record: BorrowRecord): Promise<BorrowRecord>;
  update(id: string, data: Partial<BorrowRecord>): Promise<BorrowRecord>;
}