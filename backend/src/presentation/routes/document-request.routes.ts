import { Router } from 'express';
import { CreateDocumentRequestDto } from '@/application/dtos/document-request.dto';
import { documentRequestController } from '@/container';
import { validate } from '@/presentation/middleware/validate.middleware';

export const documentRequestRouter = Router();

documentRequestRouter.get('/', documentRequestController.getDocumentRequests);
documentRequestRouter.get('/:id', documentRequestController.getDocumentRequest);
documentRequestRouter.post('/', validate(CreateDocumentRequestDto), documentRequestController.createDocumentRequest);
