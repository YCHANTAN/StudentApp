import { eq, count, inArray, and } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { Course, CourseStatus } from '@/core/entities/course.entity';
import { courses } from '../schema/course.schema';

export class CoursePgRepository implements CourseRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { programId?: string; code?: string }
  ): Promise<{ data: Course[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const conditions = [];
    if (filter?.programId) conditions.push(eq(courses.programId, filter.programId));
    if (filter?.code) conditions.push(eq(courses.code, filter.code));

    const whereClause = conditions.length > 0 ? and(...conditions) : undefined;

    const data = await this.db
      .select()
      .from(courses)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db.select({ value: count() }).from(courses).where(whereClause);

    const total = totalResult?.value ? Number(totalResult.value) : 0;

    return {
      data: data.map(this.mapToEntity),
      total,
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
    const course: Course = {
      id: row.id,
      code: row.code,
      title: row.title,
      createdAt: row.createdAt,
    };

    if (row.semesterTitle) course.semesterTitle = row.semesterTitle;
    if (row.instructor) course.instructor = row.instructor;
    if (row.units !== null && row.units !== undefined) course.units = row.units;
    if (row.schedule) course.schedule = row.schedule;
    if (row.location) course.location = row.location;
    if (row.grade) course.grade = row.grade;
    if (row.waitlistStatus) course.waitlistStatus = row.waitlistStatus;
    if (row.progress !== null && row.progress !== undefined) course.progress = Number(row.progress);
    if (row.status) course.status = row.status as CourseStatus;
    if (row.tuition !== null && row.tuition !== undefined) course.tuition = Number(row.tuition);
    if (row.remainingSlots !== null && row.remainingSlots !== undefined) course.remainingSlots = row.remainingSlots;
    if (row.isLocked !== null && row.isLocked !== undefined) course.isLocked = row.isLocked;
    if (row.lockReason) course.lockReason = row.lockReason;
    if (row.programId) course.programId = row.programId;

    return course;
  }
}

