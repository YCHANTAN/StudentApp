import { Router } from 'express';
import { authController } from '@/container';

export const authRouter = Router();
authRouter.post('/initialize', authController.initializeAccount);
authRouter.post('/login', authController.login);