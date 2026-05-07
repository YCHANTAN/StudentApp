import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { CreateEnrollmentInput } from '@/application/dtos/enrollment.dto';
import type { Enrollment } from '@/core/entities/enrollment.entity';
import { CourseFullError } from '@/core/errors/domain.error';

export class CreateEnrollmentUseCase {
  constructor(
    private readonly enrollmentRepo: EnrollmentRepository,
    private readonly courseRepo: CourseRepository
  ) {}

  async execute(input: CreateEnrollmentInput): Promise<Enrollment> {
    const courses = await this.courseRepo.findByIds(input.courseIds);
    
    // Validate slots
    for (const course of courses) {
      if (course.remainingSlots !== undefined && course.remainingSlots <= 0) {
        throw new CourseFullError(course.code);
      }
    }

    // Rules from ENROLLMENT_RULES.md
    const costPerUnit = 1500;
    const registrationFee = 1000;
    const libraryFee = 500;
    const technologyFee = 800;
    
    const totalUnits = courses.reduce((sum, c) => sum + (c.units || 0), 0);
    const totalTuition = (totalUnits * costPerUnit) + registrationFee + libraryFee + technologyFee;

    const enrollment: Enrollment = {
      id: crypto.randomUUID(),
      studentId: input.studentId,
      status: 'PENDING',
      totalUnits,
      totalTuition,
      courseIds: input.courseIds,
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    return this.enrollmentRepo.save(enrollment);
  }
}
