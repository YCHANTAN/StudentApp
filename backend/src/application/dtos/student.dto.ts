// src/application/dtos/student.dto.ts
import { z } from 'zod';

// Matches CreateStudentRequest schema
export const CreateStudentDto = z.object({
  studentId: z.string().min(1, "Student ID cannot be empty"),
  fullName: z.string().min(1, "Full name cannot be empty"),
  email: z.string().email("Invalid email format"),
});

// Matches UpdateStudentRequest schema (all fields optional)
export const UpdateStudentDto = z.object({
  fullName: z.string().min(1).optional(),
  email: z.string().email().optional(),
});

// Export inferred TypeScript types so our Use Cases know what data to expect
export type CreateStudentInput = z.infer<typeof CreateStudentDto>;
export type UpdateStudentInput = z.infer<typeof UpdateStudentDto>;