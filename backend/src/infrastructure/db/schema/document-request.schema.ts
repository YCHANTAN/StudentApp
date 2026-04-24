import { pgTable, text, varchar, timestamp, integer, uuid, pgEnum } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';

export const documentTypeEnum = pgEnum('document_type', ['TOR', 'GoodMoral', 'COE']);
export const deliveryMethodEnum = pgEnum('delivery_method', ['Pickup', 'Courier']);
export const documentRequestStatusEnum = pgEnum('document_request_status', ['PROCESSING', 'ACCEPTED', 'READY_FOR_PICKUP']);

export const documentRequests = pgTable('document_requests', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  type: documentTypeEnum('type').notNull(),
  purpose: text('purpose').notNull(),
  program: varchar('program', { length: 255 }),
  yearLevel: varchar('year_level', { length: 50 }),
  copies: integer('copies'),
  deliveryMethod: deliveryMethodEnum('delivery_method'),
  reference: varchar('reference', { length: 50 }).notNull().unique(),
  status: documentRequestStatusEnum('status').notNull(),
  submittedAt: timestamp('submitted_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export type DocumentRequestRow = typeof documentRequests.$inferSelect;
export type InsertDocumentRequestRow = typeof documentRequests.$inferInsert;
