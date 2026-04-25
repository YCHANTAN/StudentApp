import type { StudentProfileRepository } from "@/application/repositories/student-profile.repository";
import type { StudentProfile } from "@/core/entities/student-profile.entity";

export class GetStudentProfilesUseCase {
  constructor(private readonly studentProfileRepo: StudentProfileRepository) {}

  async execute(): Promise<StudentProfile[]> {
    return this.studentProfileRepo.findAll();
  }
}
