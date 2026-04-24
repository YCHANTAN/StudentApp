import { eq, count } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { GradeRecordRepository } from '@/application/repositories/grade-record.repository';
import type { GradeRecord, GradeRecordStatus } from '@/core/entities/grade-record.entity';
import { gradeRecords } from '../schema/grade-record.schema';

export class GradeRecordPgRepository implements GradeRecordRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { studentId?: string }
  ): Promise<{ data: GradeRecord[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.studentId ? eq(gradeRecords.studentId, filter.studentId) : undefined;

    const data = await this.db
      .select()
      .from(gradeRecords)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db
      .select({ value: count() })
      .from(gradeRecords)
      .where(whereClause);

    return {
      data: data.map(this.mapToEntity),
      total: Number(totalResult.value),
    };
  }

  private mapToEntity(row: any): GradeRecord {
    return {
      id: row.id,
      studentId: row.studentId,
      title: row.title,
      codeCredits: row.codeCredits,
      gradePoint: row.gradePoint,
      status: row.status as GradeRecordStatus,
      semesterLabel: row.semesterLabel || undefined,
      createdAt: row.createdAt,
    };
  }
}
