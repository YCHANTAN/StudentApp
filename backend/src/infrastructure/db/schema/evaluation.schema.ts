import { pgTable, text, timestamp, uuid, integer } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';
import { courses } from './course.schema';

export const evaluations = pgTable('evaluations', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  courseId: uuid('course_id').references(() => courses.id).notNull(),
  teachingQuality: integer('teaching_quality').notNull(),
  courseMaterials: integer('course_materials').notNull(),
  punctuality: integer('punctuality').notNull(),
  comments: text('comments'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type EvaluationRow = typeof evaluations.$inferSelect;
export type InsertEvaluationRow = typeof evaluations.$inferInsert;
