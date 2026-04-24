import { Router } from 'express';
import { courseController } from '@/container';

export const courseRouter = Router();

courseRouter.get('/', courseController.getCourses);
