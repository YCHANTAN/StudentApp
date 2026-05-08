import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { CreateEnrollmentInput } from '@/application/dtos/enrollment.dto';
import type { Enrollment } from '@/core/entities/enrollment.entity';
import type { Transaction } from '@/core/entities/transaction.entity';
import { CourseFullError, ConflictError } from '@/core/errors/domain.error';

export class CreateEnrollmentUseCase {
  constructor(
    private readonly enrollmentRepo: EnrollmentRepository,
    private readonly courseRepo: CourseRepository,
    private readonly transactionRepo: TransactionRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: CreateEnrollmentInput): Promise<Enrollment> {
    let targetStudentId = input.studentId;

    // Resolve STU-ID to database UUID if necessary
    if (!this.isUuid(targetStudentId)) {
      const student = await this.studentRepo.findByStudentId(targetStudentId);
      if (student) {
        targetStudentId = student.id;
      }
    }

    const courses = await this.courseRepo.findByIds(input.courseIds);
    
    // 1. Validate slots
    for (const course of courses) {
      if (course.remainingSlots !== undefined && course.remainingSlots <= 0) {
        throw new CourseFullError(course.code);
      }
    }

    // 2. Prevent duplicate enrollments and time conflicts
    const existingEnrollments = await this.enrollmentRepo.findAll({ page: 1, limit: 100 }, { studentId: targetStudentId });
    const activeEnrollments = existingEnrollments.data.filter(e => e.status !== 'REJECTED');
    const enrolledCourseIds = activeEnrollments.flatMap(e => e.courseIds);
    const enrolledCourses = await this.courseRepo.findByIds(enrolledCourseIds);

    for (const newCourse of courses) {
      // Check for duplicate course
      if (enrolledCourseIds.includes(newCourse.id)) {
        throw new ConflictError(`Already enrolled or pending enrollment for course: ${newCourse.code} - ${newCourse.title}`);
      }

      // Check for time conflict (exact string match for now)
      if (newCourse.schedule) {
        const conflict = enrolledCourses.find(ec => ec.schedule === newCourse.schedule);
        if (conflict) {
          throw new ConflictError(`Time conflict: ${newCourse.title} conflicts with ${conflict.title} (${newCourse.schedule})`);
        }
      }
    }

    // Rules from ENROLLMENT_RULES.md
    const costPerUnit = 1500;
    const registrationFee = 1000;
    const libraryFee = 500;
    const technologyFee = 800;
    
    const totalUnits = courses.reduce((sum, c) => sum + (c.units || 0), 0);
    const totalTuition = (totalUnits * costPerUnit) + registrationFee + libraryFee + technologyFee;

    const enrollmentId = crypto.randomUUID();
    const enrollment: Enrollment = {
      id: enrollmentId,
      studentId: targetStudentId,
      status: 'APPROVED', // Auto-approving for this prototype
      totalUnits,
      totalTuition,
      courseIds: input.courseIds,
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    const savedEnrollment = await this.enrollmentRepo.save(enrollment);

    // 3. Create a FEE transaction so it shows up in Finance
    const transaction: Transaction = {
      id: crypto.randomUUID(),
      studentId: targetStudentId,
      title: `Tuition Fee - Enrollment ${new Date().getFullYear()}`,
      amount: totalTuition.toString(),
      type: 'FEE',
      status: 'COMPLETED',
      method: 'SYSTEM',
      referenceId: `ENR-${enrollmentId.substring(0, 8)}`,
      description: `Enrollment fee for ${totalUnits} units`,
      isPaid: false,
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    await this.transactionRepo.save(transaction);

    return savedEnrollment;
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
