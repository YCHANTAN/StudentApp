import { eq, count } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { Enrollment, EnrollmentStatus } from '@/core/entities/enrollment.entity';
import { enrollments, enrollmentCourses } from '../schema/enrollment.schema';

export class EnrollmentPgRepository implements EnrollmentRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { studentId?: string }
  ): Promise<{ data: Enrollment[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.studentId ? eq(enrollments.studentId, filter.studentId) : undefined;

    const rows = await this.db
      .select()
      .from(enrollments)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db
      .select({ value: count() })
      .from(enrollments)
      .where(whereClause);

    const total = totalResult?.value ? Number(totalResult.value) : 0;

    const data: Enrollment[] = [];
    for (const row of rows) {
      const coursesRows = await this.db
        .select({ courseId: enrollmentCourses.courseId })
        .from(enrollmentCourses)
        .where(eq(enrollmentCourses.enrollmentId, row.id));
      
      data.push(this.mapToEntity(row, coursesRows.map(c => c.courseId)));
    }

    return {
      data,
      total,
    };
  }

  async findById(id: string): Promise<Enrollment | null> {
    const [row] = await this.db.select().from(enrollments).where(eq(enrollments.id, id));
    if (!row) return null;

    const coursesRows = await this.db
      .select({ courseId: enrollmentCourses.courseId })
      .from(enrollmentCourses)
      .where(eq(enrollmentCourses.enrollmentId, id));

    return this.mapToEntity(row, coursesRows.map(c => c.courseId));
  }

  async save(enrollment: Enrollment): Promise<Enrollment> {
    const { courseIds, ...rest } = enrollment;

    await this.db.transaction(async (tx) => {
      await tx.insert(enrollments).values({
        ...rest,
        totalTuition: rest.totalTuition.toString(),
      });

      if (courseIds.length > 0) {
        await tx.insert(enrollmentCourses).values(
          courseIds.map(courseId => ({
            enrollmentId: enrollment.id,
            courseId,
          }))
        );
      }
    });

    return enrollment;
  }

  async update(id: string, patch: Partial<Enrollment>): Promise<Enrollment> {
    const { courseIds, ...rest } = patch;

    await this.db.transaction(async (tx) => {
      if (Object.keys(rest).length > 0) {
        const updateData: any = { ...rest, updatedAt: new Date() };
        if (rest.totalTuition) updateData.totalTuition = rest.totalTuition.toString();
        await tx.update(enrollments).set(updateData).where(eq(enrollments.id, id));
      }

      if (courseIds) {
        await tx.delete(enrollmentCourses).where(eq(enrollmentCourses.enrollmentId, id));
        if (courseIds.length > 0) {
          await tx.insert(enrollmentCourses).values(
            courseIds.map(courseId => ({
              enrollmentId: id,
              courseId,
            }))
          );
        }
      }
    });

    const updated = await this.findById(id);
    if (!updated) throw new Error('Failed to update enrollment');
    return updated;
  }

  async delete(id: string): Promise<void> {
    await this.db.transaction(async (tx) => {
      await tx.delete(enrollmentCourses).where(eq(enrollmentCourses.enrollmentId, id));
      await tx.delete(enrollments).where(eq(enrollments.id, id));
    });
  }

  private mapToEntity(row: any, courseIds: string[]): Enrollment {
    return {
      id: row.id,
      studentId: row.studentId,
      status: row.status as EnrollmentStatus,
      totalUnits: row.totalUnits,
      totalTuition: Number(row.totalTuition),
      courseIds,
      createdAt: row.createdAt,
      updatedAt: row.updatedAt,
    };
  }
}
