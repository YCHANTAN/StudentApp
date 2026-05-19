import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import { isUuid } from '@/presentation/lib/uuid.helper';

export type GetEnrollmentsInput = {
  page: number;
  limit: number;
  studentId?: string;
};

export class GetEnrollmentsUseCase {
  constructor(
    private readonly enrollmentRepo: EnrollmentRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: GetEnrollmentsInput) {
    const { page, limit, studentId } = input;
    let targetId = studentId;

    if (studentId && !isUuid(studentId)) {
      const student = await this.studentRepo.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    const filter: { studentId?: string } = {};
    if (targetId !== undefined) filter.studentId = targetId;

    return this.enrollmentRepo.findAll({ page, limit }, filter);
  }
}
