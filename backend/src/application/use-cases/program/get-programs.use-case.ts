import type { ProgramRepository } from '@/application/repositories/program.repository';
import type { ProgramCategory } from '@/core/entities/program.entity';

export type GetProgramsInput = {
  page: number;
  limit: number;
  category?: ProgramCategory;
};

export class GetProgramsUseCase {
  constructor(private readonly programRepo: ProgramRepository) {}

  async execute(input: GetProgramsInput) {
    const { page, limit, category } = input;
    return this.programRepo.findAll({ page, limit }, { category });
  }
}
