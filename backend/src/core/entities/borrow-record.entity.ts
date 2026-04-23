export type BorrowRecord = {
  id: string; 
  bookId: string; 
  studentId: string; 
  borrowedAt: Date;
  dueDate: Date; 
  returnedAt: Date | null; 
};