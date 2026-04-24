import type { LibraryBookRepository } from '@/application/repositories/library-book.repository';
import type { UpdateLibraryBookInput } from '@/application/dtos/library-book.dto';
import { NotFoundError } from '@/core/errors/domain.error';

export class UpdateLibraryBookUseCase {
  constructor(private readonly libraryBookRepo: LibraryBookRepository) {}

  async execute(id: string, input: UpdateLibraryBookInput) {
    const existing = await this.libraryBookRepo.findById(id);
    if (!existing) throw new NotFoundError('LibraryBook');

    return this.libraryBookRepo.update(id, input);
  }
}
