import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { UpdateEnrollmentInput } from '@/application/dtos/enrollment.dto';
import { NotFoundError } from '@/core/errors/domain.error';

export class UpdateEnrollmentUseCase {
  constructor(
    private readonly enrollmentRepo: EnrollmentRepository,
    private readonly courseRepo: CourseRepository
  ) {}

  async execute(id: string, input: UpdateEnrollmentInput) {
    const existing = await this.enrollmentRepo.findById(id);
    if (!existing) throw new NotFoundError('Enrollment');

    let updatedPatch: any = { ...input };

    if (input.courseIds) {
      const courses = await this.courseRepo.findByIds(input.courseIds);
      
      // Validate slots for NEWLY added courses
      const existingCourseIds = new Set(existing.courseIds);
      for (const course of courses) {
        if (!existingCourseIds.has(course.id) && course.remainingSlots !== undefined && course.remainingSlots <= 0) {
          throw new Error(`Course ${course.code} is full`);
        }
      }

      const costPerUnit = 1500;
      const registrationFee = 1000;
      const libraryFee = 500;
      const technologyFee = 800;
      
      const totalUnits = courses.reduce((sum, c) => sum + (c.units || 0), 0);
      const totalTuition = (totalUnits * costPerUnit) + registrationFee + libraryFee + technologyFee;

      updatedPatch.totalUnits = totalUnits;
      updatedPatch.totalTuition = totalTuition;
    }

    return this.enrollmentRepo.update(id, updatedPatch);
  }
}
