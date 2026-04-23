// src/infrastructure/db/schema/enrollment.schema.ts
import { pgTable, varchar, timestamp, uuid, integer, decimal, boolean, text, pgEnum } from 'drizzle-orm/pg-core';
import { subjects } from './subject-registration.schema';

export const enrollmentStatusEnum = pgEnum('enrollment_status', ['Draft', 'Confirmed', 'Adjusted']);

export const enrollments = pgTable('enrollments', {
  id: uuid('id').primaryKey().defaultRandom(),
  studentId: varchar('student_id', { length: 128 }).notNull(),
  fullName: varchar('full_name', { length: 255 }).notNull(),
  studentIdNumber: varchar('student_id_number', { length: 128 }).notNull(),
  emailAddress: varchar('email_address', { length: 255 }).notNull(),
  phoneNumber: varchar('phone_number', { length: 50 }).notNull(),
  emergencyContactName: varchar('emergency_contact_name', { length: 255 }).notNull(),
  relationship: varchar('relationship', { length: 100 }).notNull(),
  emergencyPhone: varchar('emergency_phone', { length: 50 }).notNull(),
  selectedCredits: integer('selected_credits').notNull(),
  estimatedTuition: decimal('estimated_tuition', { precision: 12, scale: 2 }).notNull(),
  status: enrollmentStatusEnum('status').notNull().default('Draft'),
  paymentMethod: varchar('payment_method', { length: 100 }).notNull(),
  isPaid: boolean('is_paid').notNull().default(false),
  createdAt: timestamp('created_at').defaultNow().notNull(),
  updatedAt: timestamp('updated_at').defaultNow().notNull(),
});

export const enrollmentSubjects = pgTable('enrollment_subjects', {
  enrollmentId: uuid('enrollment_id').references(() => enrollments.id).notNull(),
  subjectId: varchar('subject_id', { length: 128 }).references(() => subjects.id).notNull(),
});

export type EnrollmentRow = typeof enrollments.$inferSelect;
export type InsertEnrollmentRow = typeof enrollments.$inferInsert;
