// src/infrastructure/db/schema/auth.schema.ts
import { pgTable, varchar, timestamp, boolean, text } from 'drizzle-orm/pg-core';
import { students } from './student.schema'; 

export const studentCredentials = pgTable('student_credentials', {
  studentId: text('student_id')
    .primaryKey()
    .references(() => students.id, { onDelete: 'cascade' }), 
    
  passwordHash: varchar('password_hash', { length: 255 }).notNull(),
  requiresPasswordChange: boolean('requires_password_change').default(true),
  lastLoginAt: timestamp('last_login_at'),
});