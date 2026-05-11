import { pgTable, text, varchar, timestamp, boolean, uuid, pgEnum } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';

export const transactionTypeEnum = pgEnum('transaction_type', ['PAYMENT', 'FEE', 'REFUND']);
export const transactionStatusEnum = pgEnum('transaction_status', ['PENDING', 'COMPLETED', 'FAILED']);

export const transactions = pgTable('transactions', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  title: varchar('title', { length: 255 }).notNull(),
  type: transactionTypeEnum('type').notNull(),
  amount: varchar('amount', { length: 50 }).notNull(),
  method: varchar('method', { length: 100 }).notNull(),
  status: transactionStatusEnum('status').notNull(),
  referenceId: varchar('reference_id', { length: 50 }).notNull().unique(),
  description: text('description'),
  date: timestamp('date').defaultNow().notNull(),
  isPaid: boolean('is_paid').default(false).notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type TransactionRow = typeof transactions.$inferSelect;
export type InsertTransactionRow = typeof transactions.$inferInsert;
