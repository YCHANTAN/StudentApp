import type { IEvaluationRepository } from '@/application/repositories/evaluation.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { CreateEvaluationDto } from '@/application/dtos/evaluation.dto';
import type { EvaluationRow } from '@/infrastructure/db/schema/evaluation.schema';
import { isUuid } from '@/presentation/lib/uuid.helper';

export class SubmitEvaluationUseCase {
  constructor(
    private readonly evaluationRepository: IEvaluationRepository,
    private readonly studentRepository: StudentRepository
  ) {}

  async execute(data: CreateEvaluationDto): Promise<EvaluationRow> {
    let targetId = data.studentId;

    // Resolve STU-ID to database UUID if necessary
    if (!isUuid(data.studentId)) {
      const student = await this.studentRepository.findByStudentId(data.studentId);
      if (student) {
        targetId = student.id;
      }
    }

    // Check if student already evaluated this course
    const existing = await this.evaluationRepository.findByStudentAndCourse(targetId, data.courseId);
    if (existing) {
      throw new Error('Course already evaluated by this student');
    }

    return this.evaluationRepository.create({
      studentId: targetId,
      courseId: data.courseId,
      teachingQuality: data.teachingQuality,
      courseMaterials: data.courseMaterials,
      punctuality: data.punctuality,
      comments: data.comments,
    });
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
