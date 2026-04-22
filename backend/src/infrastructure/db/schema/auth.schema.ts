import { pgTable, varchar, timestamp, boolean } from 'drizzle-orm/pg-core';

export const studentCredentials = pgTable('student_credentials', {
  studentId: varchar('student_id', { length: 128 }).primaryKey(),
  passwordHash: varchar('password_hash', { length: 255 }).notNull(),
  requiresPasswordChange: boolean('requires_password_change').default(true), 
  lastLoginAt: timestamp('last_login_at'),
});