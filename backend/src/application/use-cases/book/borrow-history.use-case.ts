import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';

export class BorrowHistoryUseCase {
  constructor(private readonly borrowRecordRepo: BorrowRecordRepository) {}

  async execute(studentId: string) {
    return this.borrowRecordRepo.findHistoryByUserId(studentId);
  }
}