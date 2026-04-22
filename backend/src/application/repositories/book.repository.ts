import type { Book } from '@/core/entities/book.entity';

export interface BookRepository {
  findById(id: string): Promise<Book | null>;
  findByIsbn(isbn: string): Promise<Book | null>; 
  findAll(): Promise<Book[]>;
  save(book: Book): Promise<Book>;
  update(id: string, data: Partial<Book>): Promise<Book>;
  delete(id: string): Promise<void>;
}