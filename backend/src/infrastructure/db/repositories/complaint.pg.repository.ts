import { eq, count } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { ComplaintRepository } from '@/application/repositories/complaint.repository';
import type { Complaint, ComplaintStatus } from '@/core/entities/complaint.entity';
import { complaints } from '../schema/complaint.schema';

export class ComplaintPgRepository implements ComplaintRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { studentId?: string }
  ): Promise<{ data: Complaint[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.studentId ? eq(complaints.studentId, filter.studentId) : undefined;

    const data = await this.db
      .select()
      .from(complaints)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db.select({ value: count() }).from(complaints).where(whereClause);

    const total = totalResult?.value ? Number(totalResult.value) : 0;

    return {
      data: data.map(this.mapToEntity),
      total,
    };
  }

  async save(complaint: Complaint): Promise<Complaint> {
    const [row] = await this.db.insert(complaints).values(complaint).returning();
    if (!row) throw new Error('Failed to save complaint');
    return this.mapToEntity(row);
  }

  private mapToEntity(row: any): Complaint {
    return {
      id: row.id,
      studentId: row.studentId,
      title: row.title,
      status: row.status as ComplaintStatus,
      createdAt: row.createdAt,
      updatedAt: row.updatedAt,
    };
  }
}

