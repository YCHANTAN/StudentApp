import { Router } from 'express';
import { transactionController } from '@/container';

export const transactionRouter = Router();

transactionRouter.get('/', transactionController.getTransactions);
