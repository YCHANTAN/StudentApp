import { Router } from 'express';
import rateLimit from 'express-rate-limit';
import { authController } from '@/container';

export const authRouter = Router();

const loginLimiter = rateLimit({
  windowMs: 15 * 60 * 1000, 
  max: 5, 
  message: { 
    success: false, 
    error: 'Too many login attempts from this IP, please try again after 15 minutes.' 
  },
  standardHeaders: true,
  legacyHeaders: false,
});

authRouter.post('/initialize', loginLimiter, authController.initializeAccount);
authRouter.post('/login', loginLimiter, authController.login);