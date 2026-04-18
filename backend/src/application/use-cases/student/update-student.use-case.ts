import type { UpdateStudentInput } from "@/application/dtos/student.dto";
import type { StudentRepository } from "@/application/repositories/student.repository";
import type { Student } from "@/core/entities/student.entity";
import { ConflictError, NotFoundError } from "@/core/errors/domain.error";

export class UpdateStudentUseCase {
  constructor(private readonly studentRepo: StudentRepository) {}

  async execute(id: string, input: UpdateStudentInput): Promise<Student> {
    const existing = await this.studentRepo.findById(id);
    if (!existing) {
      throw new NotFoundError("Student");
    }

    if (input.email && input.email !== existing.email) {
      const emailOwner = await this.studentRepo.findByEmail(input.email);
      if (emailOwner) {
        throw new ConflictError("Email already exists");
      }
    }

    const updatePayload: Partial<Student> = {
      updatedAt: new Date(),
    };

    if (input.fullName !== undefined) {
      updatePayload.fullName = input.fullName;
    }

    if (input.email !== undefined) {
      updatePayload.email = input.email;
    }

    return this.studentRepo.update(id, updatePayload);
  }
}
