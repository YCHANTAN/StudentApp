import type { CreateStudentInput } from "@/application/dtos/student.dto";
import type { StudentRepository } from "@/application/repositories/student.repository";
import type { Student } from "@/core/entities/student.entity";
import { ConflictError } from "@/core/errors/domain.error";

export class CreateStudentUseCase {
  constructor(private readonly studentRepo: StudentRepository) {}

  async execute(input: CreateStudentInput): Promise<Student> {
    const [byStudentId, byEmail] = await Promise.all([
      this.studentRepo.findByStudentId(input.studentId),
      this.studentRepo.findByEmail(input.email),
    ]);

    if (byStudentId) {
      throw new ConflictError("Student ID already exists");
    }

    if (byEmail) {
      throw new ConflictError("Email already exists");
    }

    const now = new Date();
    const student: Student = {
      id: crypto.randomUUID(),
      studentId: input.studentId,
      fullName: input.fullName,
      email: input.email,
      createdAt: now,
      updatedAt: now,
    };

    return this.studentRepo.save(student);
  }
}
