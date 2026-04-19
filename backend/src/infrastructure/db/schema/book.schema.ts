import { pgTable, text, integer, timestamp } from 'drizzle-orm/pg-core';

export const books = pgTable('books', {
  id: text('id').primaryKey(),
  title: text('title').notNull(),
  author: text('author').notNull(),
  isbn: text('isbn').notNull().unique(),
  publisher: text('publisher').notNull(),
  publicationYear: integer('publication_year').notNull(),
  category: text('category').notNull(),
  totalCopies: integer('total_copies').notNull(),
  availableCopies: integer('available_copies').notNull(),
  location: text('location').notNull(),
  coverImageUrl: text('cover_image_url'),
  externalLink: text('external_link'),
  createdAt: timestamp('created_at', { withTimezone: true }).defaultNow().notNull(),
  updatedAt: timestamp('updated_at', { withTimezone: true }).defaultNow().notNull(),
});

export type BookRow = typeof books.$inferSelect;
export type InsertBookRow = typeof books.$inferInsert;