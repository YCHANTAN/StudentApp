import { pgTable, text, integer, timestamp, pgEnum } from 'drizzle-orm/pg-core';
import { students } from './student.schema.js';

// Strict Enums for the database
export const transactionTypeEnum = pgEnum('transaction_type', ['CHARGE', 'PAYMENT']);
export const paymentMethodEnum = pgEnum('payment_method', ['CASH', 'GCASH', 'CREDIT_CARD', 'BANK_TRANSFER', 'NONE']);
export const transactionStatusEnum = pgEnum('transaction_status', ['PENDING', 'COMPLETED', 'FAILED']);

export const transactions = pgTable('transactions', {
  id: text('id').primaryKey(),
  studentId: text('student_id').references(() => students.id).notNull(),
  type: transactionTypeEnum('transaction_type').notNull(),
  amount: integer('amount').notNull(), 
  method: paymentMethodEnum('payment_method').default('NONE').notNull(),
  status: transactionStatusEnum('transaction_status').default('PENDING').notNull(),
  referenceId: text('reference_id').notNull(),
  description: text('description').notNull(),
  createdAt: timestamp('created_at', { withTimezone: true }).defaultNow().notNull(),
});

export type TransactionRow = typeof transactions.$inferSelect;
export type InsertTransactionRow = typeof transactions.$inferInsert;