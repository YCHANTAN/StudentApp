import { z } from 'zod';

export const StockStatusDto = z.enum(['Available', 'Limited', 'OutOfStock']);
export const LibraryBookTabDto = z.enum(['Available', 'Return', 'History']);

export const CreateLibraryBookDto = z.object({
  title: z.string(),
  author: z.string(),
  rating: z.number(),
  genre: z.string(),
  availableCopies: z.number().int().min(0).default(1),
  stockLabel: z.string().optional(),
  stockStatus: StockStatusDto.optional(),
  isNew: z.boolean().optional(),
  tab: LibraryBookTabDto,
});

export const UpdateLibraryBookDto = CreateLibraryBookDto.partial();

export const LibraryBookResponseDto = z.object({
  id: z.string().uuid(),
  title: z.string(),
  author: z.string(),
  rating: z.number(),
  genre: z.string(),
  availableCopies: z.number(),
  stockLabel: z.string(),
  stockStatus: StockStatusDto,
  isNew: z.boolean(),
  tab: LibraryBookTabDto,
  createdAt: z.string().datetime(),
});

export type CreateLibraryBookInput = z.infer<typeof CreateLibraryBookDto>;
export type UpdateLibraryBookInput = z.infer<typeof UpdateLibraryBookDto>;

export const GetLibraryBooksQueryDto = z.object({
  tab: LibraryBookTabDto.optional(),
});
