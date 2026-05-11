import { pgTable, text, varchar, timestamp, integer, boolean, decimal, uuid } from 'drizzle-orm/pg-core';
import { programs } from './program.schema';

export const courses = pgTable('courses', {
  id: uuid('id').primaryKey().defaultRandom(),
  code: varchar('code', { length: 50 }).notNull(),
  title: varchar('title', { length: 255 }).notNull(),
  semesterTitle: varchar('semester_title', { length: 100 }),
  instructor: varchar('instructor', { length: 255 }),
  units: integer('units'),
  schedule: varchar('schedule', { length: 255 }),
  location: varchar('location', { length: 255 }),
  grade: varchar('grade', { length: 10 }),
  waitlistStatus: varchar('waitlist_status', { length: 100 }),
  progress: decimal('progress', { precision: 3, scale: 2 }), // 0.00 to 1.00
  status: varchar('status', { length: 50 }), // Enrolled, Completed, Waitlisted
  tuition: decimal('tuition', { precision: 10, scale: 2 }),
  remainingSlots: integer('remaining_slots').default(0),
  isLocked: boolean('is_locked').default(false),
  lockReason: text('lock_reason'),
  programId: varchar('program_id', { length: 128 }).references(() => programs.id),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type CourseRow = typeof courses.$inferSelect;
export type InsertCourseRow = typeof courses.$inferInsert;
