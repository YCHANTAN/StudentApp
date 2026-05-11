import { Router } from 'express';
import { financeController } from '@/container'; 

export const financeRouter = Router();

financeRouter.get('/balance/:studentId', financeController.getStudentBalance);
financeRouter.get('/assessment/:studentId', financeController.getAssessment);
financeRouter.get('/payment-slip/:studentId', financeController.getPaymentSlip);
financeRouter.get('/history/:studentId', financeController.getTransactionHistory);
financeRouter.post('/pay', financeController.processPayment);