import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';

export class GetStudentBalanceUseCase {
  constructor(
    private readonly transactionRepository: TransactionRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(studentId: string): Promise<{ balance: number; lastUpdated: Date | null }> {
    let targetId = studentId;

    if (studentId && !this.isUuid(studentId)) {
      const student = await this.studentRepo.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    const transactions = await this.transactionRepository.findByStudentId(targetId);

    let balance = 0;
    let lastUpdated: Date | null = null;

    // Loop through history to calculate the current balance
    for (const txn of transactions) {
      const amount = parseFloat(txn.amount.replace(/[^0-9.-]+/g, ""));
      if (isNaN(amount)) continue;

      if (txn.type === 'FEE') {
        balance += amount; // Charges increase what the student owes
      } else if (txn.type === 'PAYMENT' && txn.status === 'COMPLETED') {
        balance -= amount; // Completed payments reduce the debt
      }

      // Track the date of the most recent activity
      if (!lastUpdated || txn.createdAt > lastUpdated) {
        lastUpdated = txn.createdAt;
      }
    }

    // Prevent negative balances just in case of overpayment
    return { 
      balance: Math.max(0, balance), 
      lastUpdated 
    };
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}