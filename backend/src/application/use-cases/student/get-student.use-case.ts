import type { StudentRepository } from "@/application/repositories/student.repository";
import type { Student } from "@/core/entities/student.entity";
import { NotFoundError } from "@/core/errors/domain.error";

export class GetStudentUseCase {
  constructor(private readonly studentRepo: StudentRepository) {}

  async execute(id: string): Promise<Student> {
    const student = await this.studentRepo.findById(id);

    if (!student) {
      throw new NotFoundError("Student");
    }

    return student;
  }
}
