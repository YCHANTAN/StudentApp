import { pgTable, text, timestamp } from 'drizzle-orm/pg-core';
import { books } from './book.schema';
import { students } from './student.schema';

export const borrowRecords = pgTable('borrow_records', {
  id: text('id').primaryKey(), // UUID
  bookId: text('book_id').references(() => books.id, { onDelete: 'cascade' }).notNull(),
  studentId: text('student_id').references(() => students.id, { onDelete: 'cascade' }).notNull(),
  borrowedAt: timestamp('borrowed_at', { withTimezone: true }).defaultNow().notNull(),
  dueDate: timestamp('due_date', { withTimezone: true }).notNull(),
  returnedAt: timestamp('returned_at', { withTimezone: true }),
});