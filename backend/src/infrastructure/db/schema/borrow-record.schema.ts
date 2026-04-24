import { pgTable, text, timestamp, varchar } from 'drizzle-orm/pg-core';
import { libraryBooks } from './library-book.schema';

export const borrowRecords = pgTable('borrow_records', {
  id: text('id').primaryKey(),
  userId: text('user_id').notNull(),
  bookId: varchar('book_id', { length: 128 }).references(() => libraryBooks.id, { onDelete: 'cascade' }).notNull(),
  borrowedAt: timestamp('borrowed_at', { withTimezone: true }).defaultNow().notNull(),
  dueDate: timestamp('due_date', { withTimezone: true }).notNull(),
  returnedAt: timestamp('returned_at', { withTimezone: true }),
});

export type BorrowRecordRow = typeof borrowRecords.$inferSelect;
export type InsertBorrowRecordRow = typeof borrowRecords.$inferInsert;
