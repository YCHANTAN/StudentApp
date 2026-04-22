// src/presentation/controllers/finance.controller.ts
import type { Request, Response } from 'express'; // FIX 1: Added 'type' here
import { CreateTransactionDto } from '@/application/dtos/transaction.dto';
import type { GetStudentBalanceUseCase } from '@/application/use-cases/finance/get-balance.use-case';
import type { ProcessTransactionUseCase } from '@/application/use-cases/finance/process-transaction.use-case';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';

export class FinanceController {
  constructor(
    private readonly getBalanceUseCase: GetStudentBalanceUseCase,
    private readonly processTransactionUseCase: ProcessTransactionUseCase,
    private readonly transactionRepository: TransactionRepository
  ) {}

  // GET: /api/v1/finance/balance/:studentId
  getStudentBalance = async (req: Request<{ studentId: string }>, res: Response) => {
    try {
      const { studentId } = req.params;
      const result = await this.getBalanceUseCase.execute(studentId);
      res.status(200).json({ success: true, data: result });
    } catch (error) {
      res.status(500).json({ success: false, error: 'Failed to fetch balance' });
    }
  };

  // GET: /api/v1/finance/history/:studentId
  getTransactionHistory = async (req: Request<{ studentId: string }>, res: Response) => {
    try {
      const { studentId } = req.params;
      const history = await this.transactionRepository.findByStudentId(studentId);
      res.status(200).json({ success: true, data: history });
    } catch (error) {
      res.status(500).json({ success: false, error: 'Failed to fetch history' });
    }
  };

  // POST: /api/v1/finance/pay
  processPayment = async (req: Request, res: Response) => {
    try {
      const validatedData = CreateTransactionDto.parse(req.body);
      const transaction = await this.processTransactionUseCase.execute(validatedData);
      
      res.status(201).json({ success: true, data: transaction });
    } catch (error: any) {
      res.status(400).json({ 
        success: false, 
        error: "VALIDATION_ERROR",
        details: error.errors || error.message 
      });
    }
  };
}