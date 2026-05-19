import type { ComplaintRepository } from '@/application/repositories/complaint.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import { isUuid } from '@/presentation/lib/uuid.helper';

export type GetComplaintsInput = {
  page: number;
  limit: number;
  studentId?: string;
};

export class GetComplaintsUseCase {
  constructor(
    private readonly complaintRepo: ComplaintRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: GetComplaintsInput) {
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

    return this.complaintRepo.findAll({ page, limit }, filter);
  }
}
