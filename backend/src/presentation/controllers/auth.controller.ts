import type { Request, Response } from 'express';
import { LoginRequestDto } from '@/application/dtos/auth.dto';
import type { InitializeStudentAccountUseCase, LoginStudentUseCase } from '@/application/use-cases/auth/auth.use-cases';

export class AuthController {
  constructor(
    private readonly initAccountUseCase: InitializeStudentAccountUseCase,
    private readonly loginUseCase: LoginStudentUseCase
  ) {}

  initializeAccount = async (req: Request, res: Response) => {
    try {
      const { studentId } = req.body;
      const result = await this.initAccountUseCase.execute(studentId);
      res.status(201).json(result);
    } catch (error: any) {
      res.status(400).json({ success: false, error: error.message });
    }
  };

  login = async (req: Request, res: Response) => {
    try {
      const { studentId, password } = LoginRequestDto.parse(req.body);
      const data = await this.loginUseCase.execute(studentId, password);
      res.status(200).json({ success: true, data });
    } catch (error: any) {
      res.status(401).json({ success: false, error: error.message });
    }
  };
}