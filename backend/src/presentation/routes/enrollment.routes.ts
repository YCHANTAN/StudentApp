import { Router } from 'express';
import { CreateEnrollmentDto, UpdateEnrollmentDto } from '@/application/dtos/enrollment.dto';
import { enrollmentController } from '@/container';
import { validate } from '@/presentation/middleware/validate.middleware';

export const enrollmentRouter = Router();

enrollmentRouter.get('/', enrollmentController.getEnrollments);
enrollmentRouter.get('/study-load/student/:studentId', enrollmentController.getStudyLoad);
enrollmentRouter.get('/student/:studentId/pdf', enrollmentController.getStudyLoadPdf);
enrollmentRouter.post('/', validate(CreateEnrollmentDto), enrollmentController.create);
enrollmentRouter.put('/:id', validate(UpdateEnrollmentDto), enrollmentController.update);
enrollmentRouter.delete('/:id', enrollmentController.delete);
