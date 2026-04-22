import type { ProgramPgRepository } from '@/infrastructure/db/repositories/program.pg.repository';

export class GetProgramsUseCase {
  constructor(private readonly programRepo: ProgramPgRepository) {}
  async execute(category?: 'Undergraduate' | 'Postgraduate') {
    return this.programRepo.findAll(category);
  }
}

export class ViewProgramDetailsUseCase {
  constructor(private readonly programRepo: ProgramPgRepository) {}
  async execute(id: string) {
    const program = await this.programRepo.findById(id);
    if (!program) throw new Error('Program not found');
    return program;
  }
}

export class GetProspectusLinkUseCase {
  constructor(private readonly programRepo: ProgramPgRepository) {}
  async execute(id: string) {
    const program = await this.programRepo.findById(id);
    if (!program || !program.prospectusUrl) {
      throw new Error('Prospectus not available for this program');
    }
    // In a real app, you might generate a secure AWS S3 signed URL here
    return { url: program.prospectusUrl, filename: `${program.title}-Prospectus.pdf` };
  }
}