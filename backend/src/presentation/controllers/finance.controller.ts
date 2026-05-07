import type { NextFunction, Request, Response } from 'express';
import { CreateTransactionDto } from '@/application/dtos/transaction.dto';
import type { GetStudentBalanceUseCase } from '@/application/use-cases/finance/get-balance.use-case';
import type { ProcessTransactionUseCase } from '@/application/use-cases/finance/process-transaction.use-case';
import type { GetAssessmentUseCase } from '@/application/use-cases/finance/get-assessment.use-case';
import type { GetPaymentSlipUseCase } from '@/application/use-cases/finance/get-payment-slip.use-case';
import type { GetTransactionHistoryUseCase } from '@/application/use-cases/finance/get-transaction-history.use-case';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import { ok, created } from '@/presentation/lib/response.helper';

export class FinanceController {
  constructor(
    private readonly getBalanceUseCase: GetStudentBalanceUseCase,
    private readonly processTransactionUseCase: ProcessTransactionUseCase,
    private readonly getAssessmentUseCase: GetAssessmentUseCase,
    private readonly getPaymentSlipUseCase: GetPaymentSlipUseCase,
    private readonly getTransactionHistoryUseCase: GetTransactionHistoryUseCase,
    private readonly transactionRepository: TransactionRepository
  ) {}

  private getIdOrFail(req: Request, paramName: string = 'studentId'): string {
    const val = req.params[paramName];
    if (!val || typeof val !== 'string') {
      throw new Error(`Invalid ${paramName} parameter`);
    }
    return val;
  }

  // GET: /api/v1/finance/balance/:studentId
  getStudentBalance = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = this.getIdOrFail(req);
      const result = await this.getBalanceUseCase.execute(studentId);
      ok(res, result);
    } catch (error) {
      next(error);
    }
  };

  // GET: /api/v1/finance/assessment/:studentId
  getAssessment = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = this.getIdOrFail(req);
      const result = await this.getAssessmentUseCase.execute(studentId);
      ok(res, result);
    } catch (error) {
      next(error);
    }
  };

  // GET: /api/v1/finance/payment-slip/:studentId
  getPaymentSlip = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = this.getIdOrFail(req);
      const result = await this.getPaymentSlipUseCase.execute(studentId);
      ok(res, result);
    } catch (error) {
      next(error);
    }
  };

  // GET: /api/v1/finance/history/:studentId
  getTransactionHistory = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const studentId = this.getIdOrFail(req);
      const history = await this.getTransactionHistoryUseCase.execute(studentId);
      ok(res, history);
    } catch (error) {
      next(error);
    }
  };

  // POST: /api/v1/finance/pay
  processPayment = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const validatedData = CreateTransactionDto.parse(req.body);
      const transaction = await this.processTransactionUseCase.execute(validatedData);
      created(res, transaction);
    } catch (error) {
      next(error);
    }
  };
}
