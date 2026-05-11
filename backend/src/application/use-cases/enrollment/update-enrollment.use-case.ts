import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { UpdateEnrollmentInput } from '@/application/dtos/enrollment.dto';
import { NotFoundError, ConflictError } from '@/core/errors/domain.error';

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
      
      // 1. Prevent duplicates and conflicts with OTHER enrollments
      const studentEnrollments = await this.enrollmentRepo.findAll({ page: 1, limit: 100 }, { studentId: existing.studentId });
      const otherActiveEnrollments = studentEnrollments.data.filter(e => e.id !== id && e.status !== 'REJECTED');
      const otherEnrolledCourseIds = otherActiveEnrollments.flatMap(e => e.courseIds);
      const otherEnrolledCourses = await this.courseRepo.findByIds(otherEnrolledCourseIds);

      // 2. Validate newly added courses
      const existingCourseIds = new Set(existing.courseIds);
      for (const newCourse of courses) {
        // Check against other enrollments
        if (otherEnrolledCourseIds.includes(newCourse.id)) {
          throw new ConflictError(`Already enrolled in course: ${newCourse.code} in another enrollment record.`);
        }

        // Check for time conflicts with other enrollments
        if (newCourse.schedule) {
          const conflict = otherEnrolledCourses.find(ec => ec.schedule === newCourse.schedule);
          if (conflict) {
            throw new ConflictError(`Time conflict: ${newCourse.title} conflicts with ${conflict.title} (${newCourse.schedule}) in another enrollment.`);
          }
        }

        // Validate slots for NEWLY added courses (within this record)
        if (!existingCourseIds.has(newCourse.id) && newCourse.remainingSlots !== undefined && newCourse.remainingSlots <= 0) {
          throw new Error(`Course ${newCourse.code} is full`);
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
