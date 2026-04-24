import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { BorrowRecord } from '@/core/entities/borrow-record.entity';

export class GetBorrowHistoryUseCase {
  constructor(private readonly borrowRecordRepo: BorrowRecordRepository) {}

  async execute(userId: string): Promise<BorrowRecord[]> {
    return this.borrowRecordRepo.findHistoryByUserId(userId);
  }
}
