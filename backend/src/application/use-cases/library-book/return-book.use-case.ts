import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { ReturnBookInput } from '@/application/dtos/borrow-record.dto';
import type { BorrowRecord } from '@/core/entities/borrow-record.entity';
import { NotFoundError, ConflictError } from '@/core/errors/domain.error';

export class ReturnBookUseCase {
  constructor(
    private readonly bookRepo: LibraryBookRepository,
    private readonly borrowRecordRepo: BorrowRecordRepository,
  ) {}

  async execute(bookId: string, input: ReturnBookInput): Promise<BorrowRecord> {
    // Find the active borrowing session
    const activeRecord = await this.borrowRecordRepo.findActiveRecord(bookId, input.userId);
    
    if (!activeRecord) {
      throw new ConflictError('No active borrow record found for this user and book.');
    }

    // Find the book so we know its current available copies
    const book = await this.bookRepo.findById(bookId);
    if (!book) {
      throw new NotFoundError('Book');
    }

    // Mark as returned (stamp the current date/time)
    const updatedRecord = await this.borrowRecordRepo.update(activeRecord.id, {
      returnedAt: new Date(),
    });

    // Add the book back to the availability tracker
    await this.bookRepo.update(book.id, {
      availableCopies: book.availableCopies + 1,
    });

    return updatedRecord;
  }
}