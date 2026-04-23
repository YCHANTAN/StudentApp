// src/infrastructure/db/repositories/enrollment.pg.repository.ts
import { eq } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { Enrollment } from '@/core/entities/enrollment.entity';
import { enrollments, enrollmentSubjects } from '../schema/enrollment.schema';

export class EnrollmentPgRepository implements EnrollmentRepository {
  constructor(private readonly db: NodePgDatabase<any>) {}

  async findById(id: string): Promise<Enrollment | null> {
    const [row] = await this.db.select().from(enrollments).where(eq(enrollments.id, id));
    if (!row) return null;

    const subjectsRows = await this.db.select({ subjectId: enrollmentSubjects.subjectId })
      .from(enrollmentSubjects)
      .where(eq(enrollmentSubjects.enrollmentId, id));

    return {
      ...row,
      courseIds: subjectsRows.map(s => s.subjectId),
      estimatedTuition: Number(row.estimatedTuition),
    };
  }

  async findAll(): Promise<Enrollment[]> {
    // Basic implementation, usually needs pagination
    const allEnrollments = await this.db.select().from(enrollments);
    const result: Enrollment[] = [];

    for (const row of allEnrollments) {
      const subjectsRows = await this.db.select({ subjectId: enrollmentSubjects.subjectId })
        .from(enrollmentSubjects)
        .where(eq(enrollmentSubjects.enrollmentId, row.id));

      result.push({
        ...row,
        courseIds: subjectsRows.map(s => s.subjectId),
        estimatedTuition: Number(row.estimatedTuition),
      });
    }

    return result;
  }

  async save(enrollment: Enrollment): Promise<Enrollment> {
    const { courseIds, ...rest } = enrollment;
    
    await this.db.transaction(async (tx) => {
      await tx.insert(enrollments).values({
        ...rest,
        estimatedTuition: rest.estimatedTuition.toString(),
      });

      if (courseIds.length > 0) {
        await tx.insert(enrollmentSubjects).values(
          courseIds.map(subjectId => ({
            enrollmentId: enrollment.id,
            subjectId,
          }))
        );
      }
    });

    return enrollment;
  }

  async update(id: string, data: Partial<Enrollment>): Promise<Enrollment> {
    const { courseIds, ...rest } = data;

    await this.db.transaction(async (tx) => {
      if (Object.keys(rest).length > 0) {
        const updateData: any = { ...rest };
        if (rest.estimatedTuition) updateData.estimatedTuition = rest.estimatedTuition.toString();
        await tx.update(enrollments).set(updateData).where(eq(enrollments.id, id));
      }

      if (courseIds) {
        await tx.delete(enrollmentSubjects).where(eq(enrollmentSubjects.enrollmentId, id));
        if (courseIds.length > 0) {
          await tx.insert(enrollmentSubjects).values(
            courseIds.map(subjectId => ({
              enrollmentId: id,
              subjectId,
            }))
          );
        }
      }
    });

    const updated = await this.findById(id);
    if (!updated) throw new Error('Update failed: enrollment not found');
    return updated;
  }

  async delete(id: string): Promise<void> {
    await this.db.transaction(async (tx) => {
      await tx.delete(enrollmentSubjects).where(eq(enrollmentSubjects.enrollmentId, id));
      await tx.delete(enrollments).where(eq(enrollments.id, id));
    });
  }
}
