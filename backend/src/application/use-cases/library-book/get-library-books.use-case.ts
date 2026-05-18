import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { LibraryBookTab } from '@/core/entities/library-book.entity';

export type GetLibraryBooksInput = {
  page: number;
  limit: number;
  tab?: LibraryBookTab;
  userId?: string | undefined;
};

export class GetLibraryBooksUseCase {
  constructor(private readonly libraryBookRepo: LibraryBookRepository) {}

  async execute(input: GetLibraryBooksInput) {
    const { page, limit, tab, userId } = input;
    const filter: { tab?: LibraryBookTab } = {};
    if (tab !== undefined) filter.tab = tab;

    const { data, total } = await this.libraryBookRepo.findAll({ page, limit }, filter);

    // If userId is provided and we are fetching everything (tab is undefined)
    // or if we specifically want borrowed/history books
    if (userId) {
      const userBooks = await this.libraryBookRepo.findUserBorrowedBooks(userId);
      
      // If a specific tab was requested, we might need to filter userBooks
      const filteredUserBooks = tab 
        ? userBooks.filter(b => b.tab === tab)
        : userBooks;

      // Merge and remove duplicates if any (though available books and borrowed books should ideally be separate entries in booksState for the UI filter to work)
      // Actually, if we return both, the UI will show the book in Available tab AND in Return/History tabs.
      return {
        data: [...data, ...filteredUserBooks],
        total: total + filteredUserBooks.length
      };
    }

    return { data, total };
  }
}
