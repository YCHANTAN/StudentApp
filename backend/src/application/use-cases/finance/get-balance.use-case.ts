import type { TransactionRepository } from '@/application/repositories/transaction.repository';

export class GetStudentBalanceUseCase {
  constructor(private readonly transactionRepository: TransactionRepository) {}

  async execute(studentId: string): Promise<{ balance: number; lastUpdated: Date | null }> {
    const transactions = await this.transactionRepository.findByStudentId(studentId);

    let balance = 0;
    let lastUpdated: Date | null = null;

    // Loop through history to calculate the current balance
    for (const txn of transactions) {
      if (txn.type === 'CHARGE') {
        balance += txn.amount; // Charges increase what the student owes
      } else if (txn.type === 'PAYMENT' && txn.status === 'COMPLETED') {
        balance -= txn.amount; // Completed payments reduce the debt
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
}