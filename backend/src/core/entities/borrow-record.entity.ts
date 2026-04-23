export type BorrowRecord = {
  id: string; // UUID
  bookId: string; // Foreign key to Book
  userId: string; // Foreign key to User
  borrowedAt: Date;
  dueDate: Date; // Important for returning
  returnedAt: Date | null; // Null until returned
  
  // Optional relations for history/display
  bookTitle?: string;
  bookAuthor?: string;
};