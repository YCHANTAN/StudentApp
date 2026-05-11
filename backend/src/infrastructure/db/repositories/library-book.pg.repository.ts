import { eq, count } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { LibraryBook, LibraryBookTab } from '@/core/entities/library-book.entity';
import { libraryBooks } from '../schema/library-book.schema';

export class LibraryBookPgRepository implements LibraryBookRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { tab?: LibraryBookTab }
  ): Promise<{ data: LibraryBook[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.tab ? eq(libraryBooks.tab, filter.tab) : undefined;

    const data = await this.db
      .select()
      .from(libraryBooks)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db
      .select({ value: count() })
      .from(libraryBooks)
      .where(whereClause);

    const total = totalResult?.value ? Number(totalResult.value) : 0;

    return {
      data: data.map(this.mapToEntity),
      total,
    };
  }

  async findById(id: string): Promise<LibraryBook | null> {
    const [row] = await this.db.select().from(libraryBooks).where(eq(libraryBooks.id, id));
    return row ? this.mapToEntity(row) : null;
  }

  async update(id: string, patch: Partial<LibraryBook>): Promise<LibraryBook> {
    const [row] = await this.db
      .update(libraryBooks)
      .set(patch)
      .where(eq(libraryBooks.id, id))
      .returning();
    
    if (!row) throw new Error('Failed to update library book');
    return this.mapToEntity(row);
  }

  private mapToEntity(row: any): LibraryBook {
    return {
      id: row.id,
      title: row.title,
      author: row.author,
      rating: row.rating,
      genre: row.genre,
      stockLabel: row.stockLabel,
      stockStatus: row.stockStatus,
      availableCopies: row.availableCopies,
      isNew: row.isNew,
      tab: row.tab,
      createdAt: row.createdAt,
    };
  }
}
