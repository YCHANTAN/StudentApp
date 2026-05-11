export type StockStatus = 'Available' | 'Limited' | 'OutOfStock';
export type LibraryBookTab = 'Available' | 'Return' | 'History';

export type LibraryBook = {
  id: string;
  title: string;
  author: string;
  rating: number;
  genre: string;
  stockLabel: string;
  stockStatus: StockStatus;
  availableCopies: number;
  isNew: boolean;
  tab: LibraryBookTab;
  createdAt: Date;
};
