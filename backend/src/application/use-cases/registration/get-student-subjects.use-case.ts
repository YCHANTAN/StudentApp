// src/application/use-cases/registration/get-student-subjects.use-case.ts
import type { SubjectRegistrationPgRepository } from '@/infrastructure/db/repositories/subject-registration.pg.repository';

export class GetStudentSubjectsUseCase {
  constructor(private readonly registrationRepo: SubjectRegistrationPgRepository) {}

  async execute(studentId: string, status: 'Enrolled' | 'Completed' | 'Waitlisted') {
    if (!studentId) throw new Error('Student ID is required');
    return this.registrationRepo.findSubjectsByStudentAndStatus(studentId, status);
  }
}