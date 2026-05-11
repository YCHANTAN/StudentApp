import { randomUUID } from 'crypto';
import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { BorrowBookInput } from '@/application/dtos/borrow-record.dto';
import type { BorrowRecord } from '@/core/entities/borrow-record.entity';
import { NotFoundError, ConflictError, OutOfStockError } from '@/core/errors/domain.error';

export class BorrowBookUseCase {
  constructor(
    private readonly bookRepo: LibraryBookRepository,
    private readonly borrowRecordRepo: BorrowRecordRepository,
  ) {}

  async execute(bookId: string, input: BorrowBookInput): Promise<BorrowRecord> {
    // Find the book
    const book = await this.bookRepo.findById(bookId);
    if (!book) {
      throw new NotFoundError('Book');
    }

    // Check availability tracker
    if (book.availableCopies <= 0) {
      throw new OutOfStockError(book.title);
    }

    // Check if user already has an active borrow for this exact book
    const activeRecord = await this.borrowRecordRepo.findActiveRecord(bookId, input.userId);
    if (activeRecord) {
      throw new ConflictError('You have already borrowed this book and have not returned it yet.');
    }

    // Calculate due date (let's say 14 days from now)
    const dueDate = new Date();
    dueDate.setDate(dueDate.getDate() + 14);

    // Create the pure entity
    const newRecord: BorrowRecord = {
      id: randomUUID(),
      bookId: book.id,
      userId: input.userId,
      borrowedAt: new Date(),
      dueDate: dueDate,
      returnedAt: null, // It's active, so it hasn't been returned!
    };

    // Save the record AND update the book's availability
    await this.borrowRecordRepo.save(newRecord);
    await this.bookRepo.update(book.id, { 
      availableCopies: book.availableCopies - 1 
    });

    return newRecord;
  }
}