import type { IEvaluationRepository } from '@/application/repositories/evaluation.repository';
import type { CreateEvaluationDto } from '@/application/dtos/evaluation.dto';
import type { EvaluationRow } from '@/infrastructure/db/schema/evaluation.schema';

export class SubmitEvaluationUseCase {
  constructor(private readonly evaluationRepository: IEvaluationRepository) {}

  async execute(data: CreateEvaluationDto): Promise<EvaluationRow> {
    // Check if student already evaluated this course
    const existing = await this.evaluationRepository.findByStudentAndCourse(data.studentId, data.courseId);
    if (existing) {
      throw new Error('Course already evaluated by this student');
    }

    return this.evaluationRepository.create({
      studentId: data.studentId,
      courseId: data.courseId,
      teachingQuality: data.teachingQuality,
      courseMaterials: data.courseMaterials,
      punctuality: data.punctuality,
      comments: data.comments,
    });
  }
}
