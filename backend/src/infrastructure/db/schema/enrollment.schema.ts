import { pgTable, text, varchar, timestamp, integer, decimal, uuid, pgEnum, primaryKey } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';
import { courses } from './course.schema';

export const enrollmentStatusEnum = pgEnum('enrollment_status', ['PENDING', 'APPROVED', 'REJECTED']);

export const enrollments = pgTable('enrollments', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  status: enrollmentStatusEnum('status').notNull().default('PENDING'),
  totalUnits: integer('total_units').notNull(),
  totalTuition: decimal('total_tuition', { precision: 12, scale: 2 }).notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const enrollmentCourses = pgTable('enrollment_courses', {
  enrollmentId: uuid('enrollment_id').references(() => enrollments.id, { onDelete: 'cascade' }).notNull(),
  courseId: uuid('course_id').references(() => courses.id, { onDelete: 'cascade' }).notNull(),
}, (t) => ({
  pk: primaryKey({ columns: [t.enrollmentId, t.courseId] }),
}));

export type EnrollmentRow = typeof enrollments.$inferSelect;
export type InsertEnrollmentRow = typeof enrollments.$inferInsert;
