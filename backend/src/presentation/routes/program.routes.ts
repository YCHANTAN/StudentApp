import { Router } from 'express';
import { programController } from '@/container';

export const programRouter = Router();


programRouter.get('/', programController.getPrograms);
programRouter.get('/:id', programController.viewProgram);
programRouter.get('/:id/prospectus', programController.downloadProspectus);