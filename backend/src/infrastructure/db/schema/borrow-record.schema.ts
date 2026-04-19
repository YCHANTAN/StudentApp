import { pgTable, text, timestamp } from 'drizzle-orm/pg-core';
import { books } from './book.schema.js';

export const borrowRecords = pgTable('borrow_records', {
  id: text('id').primaryKey(),
  userId: text('user_id').notNull(), 
  bookId: text('book_id').references(() => books.id, { onDelete: 'cascade' }).notNull(),
  borrowedAt: timestamp('borrowed_at', { withTimezone: true }).defaultNow().notNull(),
  dueDate: timestamp('due_date', { withTimezone: true }).notNull(),
  returnedAt: timestamp('returned_at', { withTimezone: true }),
});

export type BorrowRecordRow = typeof borrowRecords.$inferSelect;
export type InsertBorrowRecordRow = typeof borrowRecords.$inferInsert;