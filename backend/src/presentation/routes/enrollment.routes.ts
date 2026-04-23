// src/presentation/routes/enrollment.routes.ts
import { Router } from 'express';
import { validate } from '@/presentation/middleware/validate.middleware';
import { CreateEnrollmentDto } from '@/application/dtos/enrollment.dto';
import { enrollmentController } from '@/container';

export const enrollmentRouter = Router();

enrollmentRouter.post('/', validate(CreateEnrollmentDto), enrollmentController.create);
