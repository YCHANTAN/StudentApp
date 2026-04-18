import { z } from "zod";

export const CreateStudentDto = z.object({
  studentId: z.string().min(1),
  fullName: z.string().min(1),
  email: z.string().email(),
});

export const UpdateStudentDto = z.object({
  fullName: z.string().min(1).optional(),
  email: z.string().email().optional(),
});

export type CreateStudentInput = z.infer<typeof CreateStudentDto>;
export type UpdateStudentInput = z.infer<typeof UpdateStudentDto>;
