import type { GradeRecordRepository } from '@/application/repositories/grade-record.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';

export type GetGradeRecordsInput = {
  page: number;
  limit: number;
  studentId?: string;
};

export class GetGradeRecordsUseCase {
  constructor(
    private readonly gradeRecordRepo: GradeRecordRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: GetGradeRecordsInput) {
    const { page, limit, studentId } = input;
    let targetId = studentId;

    if (studentId && !this.isUuid(studentId)) {
      const student = await this.studentRepo.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    return this.gradeRecordRepo.findAll({ page, limit }, { studentId: targetId });
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
