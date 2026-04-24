import { pgTable, text, varchar, timestamp, uuid, pgEnum } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';

export const complaintStatusEnum = pgEnum('complaint_status', ['IN_REVIEW', 'RESOLVED']);

export const complaints = pgTable('complaints', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  title: varchar('title', { length: 255 }).notNull(),
  status: complaintStatusEnum('status').notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export type ComplaintRow = typeof complaints.$inferSelect;
export type InsertComplaintRow = typeof complaints.$inferInsert;
