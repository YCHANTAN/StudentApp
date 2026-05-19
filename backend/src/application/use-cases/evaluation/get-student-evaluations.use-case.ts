import type { IEvaluationRepository } from '@/application/repositories/evaluation.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { EvaluationRow } from '@/infrastructure/db/schema/evaluation.schema';
import { isUuid } from '@/presentation/lib/uuid.helper';

export class GetStudentEvaluationsUseCase {
  constructor(
    private readonly evaluationRepository: IEvaluationRepository,
    private readonly studentRepository: StudentRepository
  ) {}

  async execute(studentId: string): Promise<EvaluationRow[]> {
    let targetId = studentId;

    // Resolve STU-ID to database UUID if necessary
    if (!isUuid(studentId)) {
      const student = await this.studentRepository.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    return this.evaluationRepository.findByStudent(targetId);
  }
}
