import { Router } from 'express';
import { scheduleEntryController } from '@/container';

export const scheduleEntryRouter = Router();

scheduleEntryRouter.get('/', scheduleEntryController.getScheduleEntries);
