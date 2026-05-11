import type { ScheduleEntryRepository } from '@/application/repositories/schedule-entry.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';

export type GetScheduleEntriesInput = {
  page: number;
  limit: number;
  studentId?: string;
};

export class GetScheduleEntriesUseCase {
  constructor(
    private readonly scheduleEntryRepo: ScheduleEntryRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: GetScheduleEntriesInput) {
    const { page, limit, studentId } = input;
    let targetId = studentId;

    if (studentId && !this.isUuid(studentId)) {
      const student = await this.studentRepo.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    const filter: { studentId?: string } = {};
    if (targetId !== undefined) filter.studentId = targetId;

    return this.scheduleEntryRepo.findAll({ page, limit }, filter);
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
