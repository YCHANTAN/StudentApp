import { Router } from 'express';
import { gradeRecordController } from '@/container';

export const gradeRecordRouter = Router();

gradeRecordRouter.get('/', gradeRecordController.getGradeRecords);
