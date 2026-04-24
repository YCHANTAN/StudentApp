import { pgTable, text, varchar, timestamp, uuid } from 'drizzle-orm/pg-core';
import { studentProfiles } from './student-profile.schema';

export const scheduleEntries = pgTable('schedule_entries', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: text('student_id').references(() => studentProfiles.id).notNull(),
  dayLabel: varchar('day_label', { length: 50 }).notNull(),
  courseCode: varchar('course_code', { length: 50 }).notNull(),
  courseTitle: varchar('course_title', { length: 255 }).notNull(),
  timeRange: varchar('time_range', { length: 100 }).notNull(),
  room: varchar('room', { length: 100 }).notNull(),
  instructor: varchar('instructor', { length: 255 }).notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type ScheduleEntryRow = typeof scheduleEntries.$inferSelect;
export type InsertScheduleEntryRow = typeof scheduleEntries.$inferInsert;
