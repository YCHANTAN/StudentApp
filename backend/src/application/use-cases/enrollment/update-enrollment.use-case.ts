import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { UpdateEnrollmentInput } from '@/application/dtos/enrollment.dto';
import { NotFoundError } from '@/core/errors/domain.error';

export class UpdateEnrollmentUseCase {
  constructor(private readonly enrollmentRepo: EnrollmentRepository) {}

  async execute(id: string, input: UpdateEnrollmentInput) {
    const existing = await this.enrollmentRepo.findById(id);
    if (!existing) throw new NotFoundError('Enrollment');

    return this.enrollmentRepo.update(id, input);
  }
}
