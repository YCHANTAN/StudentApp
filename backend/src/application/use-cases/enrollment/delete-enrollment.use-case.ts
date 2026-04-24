import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import { NotFoundError } from '@/core/errors/domain.error';

export class DeleteEnrollmentUseCase {
  constructor(private readonly enrollmentRepo: EnrollmentRepository) {}

  async execute(id: string) {
    const existing = await this.enrollmentRepo.findById(id);
    if (!existing) throw new NotFoundError('Enrollment');

    await this.enrollmentRepo.delete(id);
  }
}
