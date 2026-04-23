// src/application/use-cases/enrollment/create-enrollment.use-case.ts
import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { SubjectRepository } from '@/application/repositories/subject.repository';
import type { CreateEnrollmentInput } from '@/application/dtos/enrollment.dto';
import type { Enrollment } from '@/core/entities/enrollment.entity';
import { DomainError } from '@/core/errors/domain.error';

export class CreateEnrollmentUseCase {
  // Rules from .codex/ENROLLMENT_RULES.md
  private readonly COST_PER_UNIT = 1500;
  private readonly REGISTRATION_FEE = 1000;
  private readonly LIBRARY_FEE = 500;
  private readonly TECHNOLOGY_FEE = 800;

  constructor(
    private readonly enrollmentRepo: EnrollmentRepository,
    private readonly subjectRepo: SubjectRepository
  ) {}

  async execute(input: CreateEnrollmentInput): Promise<Enrollment> {
    const selectedSubjects = await this.subjectRepo.findByIds(input.courseIds);
    
    if (selectedSubjects.length !== input.courseIds.length) {
      throw new DomainError('One or more selected courses are invalid', 'INVALID_COURSES');
    }

    const totalUnits = selectedSubjects.reduce((sum, s) => sum + s.units, 0);
    
    // Tuition formula from ENROLLMENT_RULES.md:
    // Total Tuition = (Units × Cost per unit) + Registration Fee + Library Fee + Technology Fee
    const tuitionAmount = (totalUnits * this.COST_PER_UNIT) 
                        + this.REGISTRATION_FEE 
                        + this.LIBRARY_FEE 
                        + this.TECHNOLOGY_FEE;

    const enrollment: Enrollment = {
      id: crypto.randomUUID(),
      studentId: input.studentId,
      courseIds: input.courseIds,
      fullName: input.fullName,
      studentIdNumber: input.studentIdNumber,
      emailAddress: input.emailAddress,
      phoneNumber: input.phoneNumber,
      emergencyContactName: input.emergencyContactName,
      relationship: input.relationship,
      emergencyPhone: input.emergencyPhone,
      selectedCredits: totalUnits,
      estimatedTuition: tuitionAmount,
      status: 'Confirmed', // Automatically confirm on successful creation for this demo
      paymentMethod: input.paymentMethod,
      isPaid: false, // Default to false until payment is processed
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    return this.enrollmentRepo.save(enrollment);
  }
}
