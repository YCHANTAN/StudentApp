import { eq, and } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import { evaluations } from '../schema/evaluation.schema';
import type { EvaluationRow, InsertEvaluationRow } from '../schema/evaluation.schema';
import type { IEvaluationRepository } from '@/application/repositories/evaluation.repository';
import type { Evaluation } from '@/core/entities/evaluation.entity';

export class EvaluationPgRepository implements IEvaluationRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async create(data: InsertEvaluationRow): Promise<EvaluationRow> {
    const [result] = await this.db.insert(evaluations).values(data).returning();
    if (!result) {
      throw new Error('Evaluation creation failed: no record returned');
    }
    return result;
  }

  async findByStudentAndCourse(studentId: string, courseId: string): Promise<EvaluationRow | null> {
    const [result] = await this.db
      .select()
      .from(evaluations)
      .where(and(eq(evaluations.studentId, studentId), eq(evaluations.courseId, courseId)))
      .limit(1);
    return result || null;
  }

  async findByStudent(studentId: string): Promise<EvaluationRow[]> {
    return this.db.select().from(evaluations).where(eq(evaluations.studentId, studentId));
  }
}
