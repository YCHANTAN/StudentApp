import type { LibraryBook, StockStatus, LibraryBookTab } from '@/core/entities/library-book.entity';

export interface LibraryBookRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { tab?: LibraryBookTab }): Promise<{ data: LibraryBook[]; total: number }>;
  findById(id: string): Promise<LibraryBook | null>;
  update(id: string, patch: Partial<LibraryBook>): Promise<LibraryBook>;
}
