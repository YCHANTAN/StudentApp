import { pgTable, text, varchar, timestamp, boolean, uuid } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';

export const transactions = pgTable('transactions', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  title: varchar('title', { length: 255 }).notNull(),
  date: timestamp('date').notNull(),
  amount: varchar('amount', { length: 50 }).notNull(),
  isPaid: boolean('is_paid').default(false).notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type TransactionRow = typeof transactions.$inferSelect;
export type InsertTransactionRow = typeof transactions.$inferInsert;
