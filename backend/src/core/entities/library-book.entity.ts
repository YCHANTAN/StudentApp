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

export function calculateStockInfo(availableCopies: number): { stockStatus: StockStatus; stockLabel: string } {
  const stockStatus: StockStatus = 
    availableCopies === 0 ? 'OutOfStock' : 
    (availableCopies <= 2 ? 'Limited' : 'Available');
  
  const stockLabel = 
    availableCopies === 0 ? 'Out of Stock' : 
    `${availableCopies} Copies Available`;

  return { stockStatus, stockLabel };
}
