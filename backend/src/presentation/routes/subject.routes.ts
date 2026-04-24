// src/presentation/routes/subject.routes.ts
import { Router } from 'express';
import { validate } from '@/presentation/middleware/validate.middleware';
import { CreateSubjectDto } from '@/application/dtos/subject.dto';
import { subjectController } from '@/container';

export const subjectRouter = Router();

subjectRouter.post('/', validate(CreateSubjectDto), subjectController.create);
