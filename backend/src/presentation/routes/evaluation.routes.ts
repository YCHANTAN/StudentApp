import { Router } from 'express';
import { evaluationController } from '@/container';

const router = Router();

router.post('/', evaluationController.submitEvaluation);
router.get('/student/:studentId', evaluationController.getStudentEvaluations);

export default router;
