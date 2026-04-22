import { Router } from 'express';
import { financeController } from '@/container'; 

export const financeRouter = Router();

financeRouter.get('/balance/:studentId', financeController.getStudentBalance);
financeRouter.get('/history/:studentId', financeController.getTransactionHistory);
financeRouter.post('/pay', financeController.processPayment);