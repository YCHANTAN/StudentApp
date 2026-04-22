import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import type { AuthPgRepository } from '@/infrastructure/db/repositories/auth.pg.repository';
import type { StudentPgRepository } from '@/infrastructure/db/repositories/student.pg.repository';

const JWT_SECRET = 'evsu_super_secret_key_2026';

export class InitializeStudentAccountUseCase {
  constructor(
    private readonly authRepo: AuthPgRepository,
    private readonly studentRepo: StudentPgRepository
  ) {}

  async execute(studentId: string) {
    const studentExists = await this.studentRepo.findByStudentId(studentId);
    if (!studentExists) {
      throw new Error(`Cannot initialize account: Student ID ${studentId} does not exist in the system.`);
    }

    const accountExists = await this.authRepo.findCredentialsByStudentId(studentId);
    if (accountExists) {
      throw new Error(`Account for ${studentId} has already been initialized.`);
    }

    const lastFourDigits = studentId.slice(-4);
    const defaultPassword = `cde${lastFourDigits}`;

    const salt = await bcrypt.genSalt(10);
    const passwordHash = await bcrypt.hash(defaultPassword, salt);

    await this.authRepo.saveCredentials(studentId, passwordHash);
    
    return { 
      success: true, 
      message: `Account successfully initialized for ${studentExists.fullName}.` 
    };
  }
}

export class LoginStudentUseCase {
  constructor(private readonly authRepo: AuthPgRepository) {}

  async execute(studentId: string, rawPasswordInput: string) {
    const creds = await this.authRepo.findCredentialsByStudentId(studentId);
    if (!creds) throw new Error('Invalid School ID or Password');

    const isPasswordValid = await bcrypt.compare(rawPasswordInput, creds.passwordHash);
    if (!isPasswordValid) throw new Error('Invalid School ID or Password');

    const token = jwt.sign(
      { studentId: creds.studentId }, 
      JWT_SECRET, 
      { expiresIn: '8h' }
    );

    return { 
      token, 
      studentId: creds.studentId, 
      requiresPasswordChange: creds.requiresPasswordChange 
    };
  }
}