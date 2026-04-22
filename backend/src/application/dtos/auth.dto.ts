import { z } from 'zod';

export const LoginRequestDto = z.object({
  studentId: z.string().min(1, 'School ID is required'),
  password: z.string().min(1, 'Password is required'),
});