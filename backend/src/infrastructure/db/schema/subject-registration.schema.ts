import { pgTable, varchar, timestamp, pgEnum, uuid, integer, decimal } from 'drizzle-orm/pg-core';

export const registrationStatusEnum = pgEnum('registration_status', ['Enrolled', 'Completed', 'Waitlisted']);

export const subjects = pgTable('subjects', {
  id: varchar('id', { length: 128 }).primaryKey(), // e.g., 'CS301'
  title: varchar('title', { length: 255 }).notNull(),
  units: integer('units').notNull(),
});

export const sections = pgTable('sections', {
  id: varchar('id', { length: 128 }).primaryKey(), // e.g., 'CS301-A'
  subjectId: varchar('subject_id', { length: 128 }).notNull(),
  term: varchar('term', { length: 128 }).notNull(), 
  instructorName: varchar('instructor_name', { length: 255 }).notNull(),
  timeSlots: varchar('time_slots', { length: 255 }).notNull(), 
  location: varchar('location', { length: 255 }).notNull(), 
});

export const subjectRegistrations = pgTable('subject_registrations', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: varchar('student_id', { length: 128 }).notNull(),
  sectionId: varchar('section_id', { length: 128 }).notNull(),
  status: registrationStatusEnum('status').notNull(),
  progressPercentage: decimal('progress_percentage', { precision: 5, scale: 2 }), 
  createdAt: timestamp('created_at').defaultNow(),
});