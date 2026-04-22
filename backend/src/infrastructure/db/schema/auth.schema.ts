// src/infrastructure/db/schema/auth.schema.ts
import { pgTable, varchar, timestamp, boolean } from 'drizzle-orm/pg-core';
import { students } from './student.schema';

export const studentCredentials = pgTable('student_credentials', {
  studentId: varchar('student_id', { length: 128 })
    .primaryKey()
    .references(() => students.studentId, { onDelete: 'cascade' }), 
    
  passwordHash: varchar('password_hash', { length: 255 }).notNull(),
  requiresPasswordChange: boolean('requires_password_change').default(true),
  lastLoginAt: timestamp('last_login_at'),
});