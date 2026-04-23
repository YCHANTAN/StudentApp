// src/presentation/controllers/enrollment.controller.ts
import type { Request, Response, NextFunction } from 'express';
import type { CreateEnrollmentUseCase } from '@/application/use-cases/enrollment/create-enrollment.use-case';
import { created, ok } from '@/presentation/lib/response.helper';

export class EnrollmentController {
  constructor(
    private readonly createEnrollmentUseCase: CreateEnrollmentUseCase
  ) {}

  create = async (req: Request, res: Response, next: NextFunction) => {
    try {
      const enrollment = await this.createEnrollmentUseCase.execute(req.body);
      
      // Breakdown calculation for UI feedback as requested in requirement 3
      // Although computed in Use Case, we can provide extra detail in the response if needed
      // Rules from ENROLLMENT_RULES.md
      const costPerUnit = 1500;
      const registrationFee = 1000;
      const libraryFee = 500;
      const technologyFee = 800;
      const tuitionOnly = enrollment.selectedCredits * costPerUnit;

      const breakdown = {
        tuitionFee: tuitionOnly,
        registrationFee,
        libraryFee,
        technologyFee,
        totalAmount: enrollment.estimatedTuition
      };

      created(res, { 
        enrollment,
        breakdown,
        message: "Enrollment Successful" // Requirement 5
      });
    } catch (err) {
      next(err);
    }
  };
}
