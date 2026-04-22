export type Book = {
  id: string;
  title: string;
  author: string;
  isbn: string;
  publisher: string;
  publicationYear: number;
  category: string;
  totalCopies: number;
  availableCopies: number;
  location: string;
  coverImageUrl: string | null;
  externalLink: string | null;
  createdAt: Date;
  updatedAt: Date;
};