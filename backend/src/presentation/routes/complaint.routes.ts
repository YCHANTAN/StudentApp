import { Router } from 'express';
import { CreateComplaintDto } from '@/application/dtos/complaint.dto';
import { complaintController } from '@/container';
import { validate } from '@/presentation/middleware/validate.middleware';

export const complaintRouter = Router();

complaintRouter.get('/', complaintController.getComplaints);
complaintRouter.post('/', validate(CreateComplaintDto), complaintController.createComplaint);
