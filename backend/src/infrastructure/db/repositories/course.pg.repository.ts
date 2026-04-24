import { eq, count, inArray } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { Course, CourseStatus } from '@/core/entities/course.entity';
import { courses } from '../schema/course.schema';

export class CoursePgRepository implements CourseRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { programId?: string }
  ): Promise<{ data: Course[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.programId ? eq(courses.programId, filter.programId) : undefined;

    const data = await this.db
      .select()
      .from(courses)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db
      .select({ value: count() })
      .from(courses)
      .where(whereClause);

    return {
      data: data.map(this.mapToEntity),
      total: Number(totalResult.value),
    };
  }

  async findById(id: string): Promise<Course | null> {
    const [row] = await this.db.select().from(courses).where(eq(courses.id, id));
    return row ? this.mapToEntity(row) : null;
  }

  async findByIds(ids: string[]): Promise<Course[]> {
    if (ids.length === 0) return [];
    const rows = await this.db.select().from(courses).where(inArray(courses.id, ids));
    return rows.map(this.mapToEntity);
  }

  private mapToEntity(row: any): Course {
    return {
      id: row.id,
      code: row.code,
      title: row.title,
      semesterTitle: row.semesterTitle || undefined,
      instructor: row.instructor || undefined,
      units: row.units || undefined,
      schedule: row.schedule || undefined,
      location: row.location || undefined,
      grade: row.grade || undefined,
      waitlistStatus: row.waitlistStatus || undefined,
      progress: row.progress ? Number(row.progress) : undefined,
      status: row.status as CourseStatus | undefined,
      tuition: row.tuition ? Number(row.tuition) : undefined,
      isLocked: row.isLocked ?? undefined,
      lockReason: row.lockReason || undefined,
      programId: row.programId || undefined,
      createdAt: row.createdAt,
    };
  }
}
