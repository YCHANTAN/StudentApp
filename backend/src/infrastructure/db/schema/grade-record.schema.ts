import { pgTable, text, varchar, timestamp, uuid } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';

export const gradeRecords = pgTable('grade_records', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  title: varchar('title', { length: 255 }).notNull(),
  codeCredits: varchar('code_credits', { length: 100 }).notNull(),
  gradePoint: varchar('grade_point', { length: 20 }).notNull(),
  status: varchar('status', { length: 50 }).notNull(), // COMPLETED, IN_PROGRESS
  semesterLabel: varchar('semester_label', { length: 100 }),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type GradeRecordRow = typeof gradeRecords.$inferSelect;
export type InsertGradeRecordRow = typeof gradeRecords.$inferInsert;
