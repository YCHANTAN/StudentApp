import { pgTable, text, integer, timestamp, pgEnum } from 'drizzle-orm/pg-core';
import { students } from './student.schema.js';

export const documentTypeEnum = pgEnum('document_type', ['TOR', 'GOOD_MORAL', 'COE']);
export const requestStatusEnum = pgEnum('request_status', ['PENDING', 'PROCESSING', 'READY', 'COMPLETED', 'REJECTED']);

export const documentRequests = pgTable('document_requests', {
  id: text('id').primaryKey(),
  studentId: text('student_id').references(() => students.id).notNull(),
  documentType: documentTypeEnum('document_type').notNull(),
  purpose: text('purpose').notNull(),
  copies: integer('copies').default(1).notNull(),
  status: requestStatusEnum('request_status').default('PENDING').notNull(),
  notes: text('notes'),
  requestedAt: timestamp('requested_at', { withTimezone: true }).defaultNow().notNull(),
  updatedAt: timestamp('updated_at', { withTimezone: true }).defaultNow().notNull(),
});

export type DocumentRequestRow = typeof documentRequests.$inferSelect;
export type InsertDocumentRequestRow = typeof documentRequests.$inferInsert;