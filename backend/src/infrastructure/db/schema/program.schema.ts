import { pgTable, text, varchar, timestamp, pgEnum } from 'drizzle-orm/pg-core';

export const programCategoryEnum = pgEnum('program_category', ['Undergraduate', 'Postgraduate']);
export const badgeVariantEnum = pgEnum('badge_variant', ['Accent', 'Primary']);

export const programs = pgTable('programs', {
  id: varchar('id', { length: 128 }).primaryKey(),
  title: varchar('title', { length: 255 }).notNull(),
  badgeText: varchar('badge_text', { length: 100 }).notNull(),
  badgeVariant: badgeVariantEnum('badge_variant').notNull(),
  scheduleLine: varchar('schedule_line', { length: 255 }).notNull(),
  description: text('description').notNull(),
  category: programCategoryEnum('category').notNull(),
  prospectusUrl: text('prospectus_url'),
  createdAt: timestamp('created_at').defaultNow().notNull(),
});

export type ProgramRow = typeof programs.$inferSelect;
export type InsertProgramRow = typeof programs.$inferInsert;
