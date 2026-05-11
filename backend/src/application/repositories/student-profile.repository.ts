import type { StudentProfile } from "@/core/entities/student-profile.entity";

export interface StudentProfileRepository {
  findById(id: string): Promise<StudentProfile | null>;
  findAll(): Promise<StudentProfile[]>;
  update(id: string, patch: Partial<StudentProfile>): Promise<StudentProfile>;
}
