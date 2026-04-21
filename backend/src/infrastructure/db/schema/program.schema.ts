import { pgTable, text, varchar, timestamp, pgEnum } from 'drizzle-orm/pg-core';

// Define the two categories
export const categoryEnum = pgEnum('program_category', ['Undergraduate', 'Postgraduate']);

export const programs = pgTable('programs', {
  id: varchar('id', { length: 128 }).primaryKey(),
  title: varchar('title', { length: 255 }).notNull(),
  category: categoryEnum('category').notNull(),
  status: varchar('status', { length: 100 }), // e.g., "ENROLLMENT OPEN"
  duration: varchar('duration', { length: 100 }), // e.g., "4 Years • Full Time"
  description: text('description'),
  prospectusUrl: varchar('prospectus_url', { length: 500 }), // Link to the PDF
  createdAt: timestamp('created_at').defaultNow(),
});