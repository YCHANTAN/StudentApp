import type { EvaluationRow, InsertEvaluationRow } from '@/infrastructure/db/schema/evaluation.schema';

export interface IEvaluationRepository {
  create(data: InsertEvaluationRow): Promise<EvaluationRow>;
  findByStudentAndCourse(studentId: string, courseId: string): Promise<EvaluationRow | null>;
  findByStudent(studentId: string): Promise<EvaluationRow[]>;
}
