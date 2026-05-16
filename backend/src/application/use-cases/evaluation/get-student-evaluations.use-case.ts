import type { IEvaluationRepository } from '@/application/repositories/evaluation.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { EvaluationRow } from '@/infrastructure/db/schema/evaluation.schema';

export class GetStudentEvaluationsUseCase {
  constructor(
    private readonly evaluationRepository: IEvaluationRepository,
    private readonly studentRepository: StudentRepository
  ) {}

  async execute(studentId: string): Promise<EvaluationRow[]> {
    let targetId = studentId;

    // Resolve STU-ID to database UUID if necessary
    if (!this.isUuid(studentId)) {
      const student = await this.studentRepository.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    return this.evaluationRepository.findByStudent(targetId);
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
