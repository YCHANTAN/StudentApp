import type { StudentRepository } from "@/application/repositories/student.repository";
import { NotFoundError } from "@/core/errors/domain.error";

export class DeleteStudentUseCase {
  constructor(private readonly studentRepo: StudentRepository) {}

  async execute(id: string): Promise<void> {
    const existing = await this.studentRepo.findById(id);
    if (!existing) {
      throw new NotFoundError("Student");
    }

    await this.studentRepo.delete(id);
  }
}
