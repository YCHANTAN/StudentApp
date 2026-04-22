// src/presentation/routes/document.routes.ts
import { Router } from 'express';
import { validate } from '../middleware/validate.middleware.js';
import { CreateDocumentRequestDto } from '../../application/dtos/document-request.dto.js';
import { documentController } from '../../container.js';

export const documentRouter = Router();


documentRouter.post(
  '/request',
  validate(CreateDocumentRequestDto),
  documentController.createRequest
);