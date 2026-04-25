import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { UpdateLibraryBookInput } from '@/application/dtos/library-book.dto';
import type { LibraryBook } from '@/core/entities/library-book.entity';
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
    if (input.stockLabel !== undefined) patch.stockLabel = input.stockLabel;
    if (input.stockStatus !== undefined) patch.stockStatus = input.stockStatus;
    if (input.isNew !== undefined) patch.isNew = input.isNew;
    if (input.tab !== undefined) patch.tab = input.tab;

    return this.libraryBookRepo.update(id, patch);
  }
}
