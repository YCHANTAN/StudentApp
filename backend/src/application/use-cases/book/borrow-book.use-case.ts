import { v4 as uuidv4 } from 'uuid';
import type { BookRepository } from '@/application/repositories/book.repository';
import type { BorrowRecordRepository } from '@/application/repositories/borrow-record.repository';
import type { BorrowBookInput } from '@/application/dtos/borrow-record.dto';
import type { BorrowRecord } from '@/core/entities/borrow-record.entity';

export class BorrowBookUseCase {
  constructor(
    private readonly bookRepo: BookRepository,
    private readonly borrowRecordRepo: BorrowRecordRepository,
  ) {}

  async execute(bookId: string, input: BorrowBookInput): Promise<BorrowRecord> {
    const book = await this.bookRepo.findById(bookId);
    if (!book) throw new Error('Book not found');
    if (book.availableCopies <= 0) throw new Error('No copies available');

    const activeRecord = await this.borrowRecordRepo.findActiveRecord(bookId, input.studentId);
    if (activeRecord) throw new Error('Student already has an active borrow record for this book');

    const dueDate = new Date();
    dueDate.setDate(dueDate.getDate() + 14);

    const newRecord: BorrowRecord = {
      id: uuidv4(),
      bookId,
      studentId: input.studentId,
      borrowedAt: new Date(),
      dueDate,
      returnedAt: null,
    };

    const savedRecord = await this.borrowRecordRepo.save(newRecord);

    await this.bookRepo.update(book.id, { availableCopies: book.availableCopies - 1 });

    return savedRecord;
  }
}