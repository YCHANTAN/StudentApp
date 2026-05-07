import type { IEvaluationRepository } from '@/application/repositories/evaluation.repository';
import type { EvaluationRow } from '@/infrastructure/db/schema/evaluation.schema';

export class GetStudentEvaluationsUseCase {
  constructor(private readonly evaluationRepository: IEvaluationRepository) {}

  async execute(studentId: string): Promise<EvaluationRow[]> {
    return this.evaluationRepository.findByStudent(studentId);
  }
}
