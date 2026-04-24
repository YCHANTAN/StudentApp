import { eq, count } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { ScheduleEntryRepository } from '@/application/repositories/schedule-entry.repository';
import type { ScheduleEntry } from '@/core/entities/schedule-entry.entity';
import { scheduleEntries } from '../schema/schedule-entry.schema';

export class ScheduleEntryPgRepository implements ScheduleEntryRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { studentId?: string }
  ): Promise<{ data: ScheduleEntry[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.studentId ? eq(scheduleEntries.studentId, filter.studentId) : undefined;

    const data = await this.db
      .select()
      .from(scheduleEntries)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db
      .select({ value: count() })
      .from(scheduleEntries)
      .where(whereClause);

    return {
      data: data.map(this.mapToEntity),
      total: Number(totalResult.value),
    };
  }

  private mapToEntity(row: any): ScheduleEntry {
    return {
      id: row.id,
      studentId: row.studentId,
      dayLabel: row.dayLabel,
      courseCode: row.courseCode,
      courseTitle: row.courseTitle,
      timeRange: row.timeRange,
      room: row.room,
      instructor: row.instructor,
      createdAt: row.createdAt,
    };
  }
}
