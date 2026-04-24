import type { Program, ProgramCategory } from '@/core/entities/program.entity';

export interface ProgramRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { category?: ProgramCategory }): Promise<{ data: Program[]; total: number }>;
  findById(id: string): Promise<Program | null>;
}
