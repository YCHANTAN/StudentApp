import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { ReturnBookInput } from '@/application/dtos/borrow-record.dto';
import { type LibraryBook, calculateStockInfo } from '@/core/entities/library-book.entity';
import { NotFoundError, ConflictError } from '@/core/errors/domain.error';

export class ReturnBookUseCase {
  constructor(
    private readonly bookRepo: LibraryBookRepository,
    private readonly borrowRecordRepo: BorrowRecordRepository,
  ) {}

  async execute(bookId: string, input: ReturnBookInput): Promise<LibraryBook> {
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
    await this.borrowRecordRepo.update(activeRecord.id, {
      returnedAt: new Date(),
    });

    // Add the book back to the availability tracker
    const newAvailableCopies = book.availableCopies + 1;
    const { stockStatus, stockLabel } = calculateStockInfo(newAvailableCopies);

    const updatedBook = await this.bookRepo.update(book.id, {
      availableCopies: newAvailableCopies,
      stockStatus: stockStatus as any,
      stockLabel
    });

    // Return the book marked with tab=History so the UI knows it's now in history
    return {
      ...updatedBook,
      tab: 'History'
    };
  }
}