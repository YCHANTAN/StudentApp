import { v4 as uuidv4 } from 'uuid';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { CreateTransactionInput } from '@/application/dtos/transaction.dto';
import type { Transaction } from '@/core/entities/transaction.entity';

export class ProcessTransactionUseCase {
  constructor(private readonly transactionRepository: TransactionRepository) {}

  async execute(data: CreateTransactionInput): Promise<Transaction> {
    // Generate a professional-looking receipt/reference number (e.g., TXN-A1B2C3D4)
    const shortId = uuidv4().substring(0, 8).toUpperCase();
    const referenceId = `TXN-${shortId}`;

    const transaction: Transaction = {
      id: uuidv4(),
      studentId: data.studentId,
      title: data.title,
      type: data.type,
      amount: data.amount,
      method: data.method,
      // For this simulation, we will assume online payments complete instantly
      status: data.type === 'PAYMENT' ? 'COMPLETED' : 'PENDING', 
      referenceId,
      description: data.description ?? null,
      date: data.date ? new Date(data.date) : new Date(),
      isPaid: data.isPaid ?? true,
      createdAt: new Date(),
    };

    return this.transactionRepository.save(transaction);
  }
}