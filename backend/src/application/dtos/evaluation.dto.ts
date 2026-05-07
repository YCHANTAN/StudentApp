import { z } from 'zod';

export const CreateEvaluationDto = z.object({
  studentId: z.string(),
  courseId: z.string().uuid(),
  teachingQuality: z.number().int().min(1).max(5),
  courseMaterials: z.number().int().min(1).max(5),
  punctuality: z.number().int().min(1).max(5),
  comments: z.string().optional(),
});

export const EvaluationDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  courseId: z.string().uuid(),
  teachingQuality: z.number(),
  courseMaterials: z.number(),
  punctuality: z.number(),
  comments: z.string().nullable(),
  createdAt: z.date(),
});

export type CreateEvaluationDto = z.infer<typeof CreateEvaluationDto>;
export type EvaluationDto = z.infer<typeof EvaluationDto>;
