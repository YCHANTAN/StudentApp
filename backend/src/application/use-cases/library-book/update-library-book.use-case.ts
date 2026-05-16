import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { UpdateLibraryBookInput } from '@/application/dtos/library-book.dto';
import { type LibraryBook, calculateStockInfo } from '@/core/entities/library-book.entity';
import { NotFoundError } from '@/core/errors/domain.error';

export class UpdateLibraryBookUseCase {
  constructor(private readonly libraryBookRepo: LibraryBookRepository) {}

  async execute(id: string, input: UpdateLibraryBookInput) {
    const existing = await this.libraryBookRepo.findById(id);
    if (!existing) throw new NotFoundError('LibraryBook');

    const patch: Partial<LibraryBook> = {};

    if (input.title !== undefined) patch.title = input.title;
    if (input.author !== undefined) patch.author = input.author;
    if (input.rating !== undefined) patch.rating = input.rating;
    if (input.genre !== undefined) patch.genre = input.genre;
    if (input.isNew !== undefined) patch.isNew = input.isNew;
    if (input.tab !== undefined) patch.tab = input.tab;
    
    // Handle availability consistency
    if (input.availableCopies !== undefined) {
      patch.availableCopies = input.availableCopies;
      const { stockStatus, stockLabel } = calculateStockInfo(patch.availableCopies);
      patch.stockStatus = stockStatus;
      patch.stockLabel = stockLabel;
    } else {
      // If status or label are being updated manually, ensure they match the existing availableCopies
      // This prevents the inconsistency reported by the user
      if (input.stockStatus !== undefined || input.stockLabel !== undefined) {
        const { stockStatus, stockLabel } = calculateStockInfo(existing.availableCopies);
        patch.stockStatus = stockStatus;
        patch.stockLabel = stockLabel;
      }
    }

    return this.libraryBookRepo.update(id, patch);
  }
}
