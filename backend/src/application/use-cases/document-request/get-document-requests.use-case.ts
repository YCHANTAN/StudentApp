import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import { isUuid } from '@/presentation/lib/uuid.helper';

export type GetDocumentRequestsInput = {
  page: number;
  limit: number;
  studentId?: string;
};

export class GetDocumentRequestsUseCase {
  constructor(
    private readonly documentRequestRepo: DocumentRequestRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: GetDocumentRequestsInput) {
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

    return this.documentRequestRepo.findAll({ page, limit }, filter);
  }
}
