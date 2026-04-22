import { z } from 'zod';

export const CreateBookDto = z.object({
  title: z.string().min(1, "Title cannot be empty"),
  author: z.string().min(1, "Author cannot be empty"),
  isbn: z.string().min(10, "ISBN must be valid"),
  publisher: z.string().min(1),
  // Force the year to be a realistic number
  publicationYear: z.coerce.number().int().min(1000).max(new Date().getFullYear()),
  category: z.string().min(1),
  // You can't add 0 or negative books to the library
  totalCopies: z.coerce.number().int().positive(),
  location: z.string().min(1),
  coverImageUrl: z.string().url("Must be a valid URL").optional(),
  externalLink: z.string().url("Must be a valid URL").optional(),
});

// All fields are optional for an update
export const UpdateBookDto = CreateBookDto.partial();

export type CreateBookInput = z.infer<typeof CreateBookDto>;
export type UpdateBookInput = z.infer<typeof UpdateBookDto>;