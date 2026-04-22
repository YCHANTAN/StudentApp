import type { Request, Response, NextFunction } from 'express';
import { created } from '../lib/response.helper.js';
import type { CreateDocumentRequestUseCase } from '../../application/use-cases/document/create-request.use-case.js';

export class DocumentController {
  constructor(private readonly createRequestUseCase: CreateDocumentRequestUseCase) {}

  createRequest = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const requestRecord = await this.createRequestUseCase.execute(req.body);
      created(res, requestRecord);
    } catch (err) {
      next(err);
    }
  };
}