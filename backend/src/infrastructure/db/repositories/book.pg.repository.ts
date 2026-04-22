import { eq } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { BookRepository } from '@/application/repositories/book.repository';
import type { Book } from '@/core/entities/book.entity';
import { books } from '../schema/book.schema.js';

export class BookPgRepository implements BookRepository {
  constructor(private readonly db: NodePgDatabase<Record<string, never>>) {}

  async findById(id: string): Promise<Book | null> {
    const [row] = await this.db.select().from(books).where(eq(books.id, id));
    return row ?? null;
  }

  async findByIsbn(isbn: string): Promise<Book | null> {
    const [row] = await this.db.select().from(books).where(eq(books.isbn, isbn));
    return row ?? null;
  }

  async findAll(): Promise<Book[]> {
    return this.db.select().from(books);
  }

  async save(book: Book): Promise<Book> {
    const [row] = await this.db.insert(books).values(book).returning();
    if (!row) throw new Error('Failed to persist book');
    return row;
  }

  async update(id: string, data: Partial<Book>): Promise<Book> {
    const updateData = { ...data, updatedAt: new Date() };
    const [row] = await this.db.update(books).set(updateData).where(eq(books.id, id)).returning();
    if (!row) throw new Error('Failed to update book');
    return row;
  }

  async delete(id: string): Promise<void> {
    await this.db.delete(books).where(eq(books.id, id));
  }
}