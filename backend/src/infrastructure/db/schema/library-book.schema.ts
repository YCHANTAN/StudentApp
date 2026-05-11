import { pgTable, text, varchar, timestamp, pgEnum, boolean, integer, doublePrecision } from 'drizzle-orm/pg-core';

export const stockStatusEnum = pgEnum('stock_status', ['Available', 'Limited', 'OutOfStock']);
export const libraryBookTabEnum = pgEnum('library_book_tab', ['Available', 'Return', 'History']);

export const libraryBooks = pgTable('library_books', {
  id: varchar('id', { length: 128 }).primaryKey(),
  title: varchar('title', { length: 255 }).notNull(),
  author: varchar('author', { length: 255 }).notNull(),
  rating: doublePrecision('rating').notNull(),
  genre: varchar('genre', { length: 100 }).notNull(),
  stockLabel: varchar('stock_label', { length: 100 }).notNull(),
  stockStatus: stockStatusEnum('stock_status').notNull(),
  availableCopies: integer('available_copies').default(0).notNull(),
  isNew: boolean('is_new').default(false).notNull(),
  tab: libraryBookTabEnum('tab').notNull(),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type LibraryBookRow = typeof libraryBooks.$inferSelect;
export type InsertLibraryBookRow = typeof libraryBooks.$inferInsert;
