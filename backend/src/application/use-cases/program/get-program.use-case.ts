import type { ProgramRepository } from '@/application/repositories/program.repository';
import { NotFoundError } from '@/core/errors/domain.error';

export class GetProgramUseCase {
  constructor(private readonly programRepo: ProgramRepository) {}

  async execute(id: string) {
    const program = await this.programRepo.findById(id);
    if (!program) throw new NotFoundError('Program');
    return program;
  }
}
