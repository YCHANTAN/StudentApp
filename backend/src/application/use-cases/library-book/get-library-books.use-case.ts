import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { LibraryBookTab } from '@/core/entities/library-book.entity';

export type GetLibraryBooksInput = {
  page: number;
  limit: number;
  tab?: LibraryBookTab;
};

export class GetLibraryBooksUseCase {
  constructor(private readonly libraryBookRepo: LibraryBookRepository) {}

  async execute(input: GetLibraryBooksInput) {
    const { page, limit, tab } = input;
    const filter: { tab?: LibraryBookTab } = {};
    if (tab !== undefined) filter.tab = tab;

    return this.libraryBookRepo.findAll({ page, limit }, filter);
  }
}
