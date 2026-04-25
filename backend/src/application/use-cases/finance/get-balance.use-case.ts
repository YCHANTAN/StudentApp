import type { TransactionRepository } from '@/application/repositories/transaction.repository';

export class GetStudentBalanceUseCase {
  constructor(private readonly transactionRepository: TransactionRepository) {}

  async execute(studentId: string): Promise<{ balance: number; lastUpdated: Date | null }> {
    const transactions = await this.transactionRepository.findByStudentId(studentId);

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
}