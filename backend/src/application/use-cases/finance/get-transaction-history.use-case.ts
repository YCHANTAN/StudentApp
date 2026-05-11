import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { Transaction } from '@/core/entities/transaction.entity';

export class GetTransactionHistoryUseCase {
  constructor(
    private readonly transactionRepository: TransactionRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(studentId: string): Promise<Transaction[]> {
    let targetId = studentId;

    if (studentId && !this.isUuid(studentId)) {
      const student = await this.studentRepo.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    return this.transactionRepository.findByStudentId(targetId);
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
