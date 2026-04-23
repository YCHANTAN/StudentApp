import { pgTable, text, varchar, timestamp, integer } from 'drizzle-orm/pg-core';

export const students = pgTable('students', {
  id: text('id').primaryKey(), 

  studentNumber: varchar('student_number', { length: 128 }).notNull().unique(), 
  
  fullName: varchar('full_name', { length: 255 }).notNull(),
  email: varchar('email', { length: 255 }).notNull().unique(),
  course: varchar('course', { length: 255 }), 
  yearLevel: integer('year_level'),
  createdAt: timestamp('created_at', { withTimezone: true }).defaultNow().notNull(),
  updatedAt: timestamp('updated_at', { withTimezone: true }).defaultNow().notNull(),
});