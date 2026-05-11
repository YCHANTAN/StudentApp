import { eq, count, and } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { ProgramRepository } from '@/application/repositories/program.repository';
import type { Program, ProgramCategory } from '@/core/entities/program.entity';
import { programs } from '../schema/program.schema';

export class ProgramPgRepository implements ProgramRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { category?: ProgramCategory }
  ): Promise<{ data: Program[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.category ? eq(programs.category, filter.category) : undefined;

    const data = await this.db
      .select()
      .from(programs)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db.select({ value: count() }).from(programs).where(whereClause);

    const total = totalResult?.value ? Number(totalResult.value) : 0;

    return {
      data: data.map(this.mapToEntity),
      total,
    };
  }

  async findById(id: string): Promise<Program | null> {
    const [row] = await this.db.select().from(programs).where(eq(programs.id, id));
    return row ? this.mapToEntity(row) : null;
  }

  private mapToEntity(row: any): Program {
    return {
      id: row.id,
      title: row.title,
      badgeText: row.badgeText,
      badgeVariant: row.badgeVariant,
      scheduleLine: row.scheduleLine,
      description: row.description,
      category: row.category,
      prospectusUrl: row.prospectusUrl,
      createdAt: row.createdAt,
    };
  }
}

