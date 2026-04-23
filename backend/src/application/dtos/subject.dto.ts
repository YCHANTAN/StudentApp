// src/application/dtos/subject.dto.ts
import { z } from 'zod';

export const CreateSubjectDto = z.object({
  id: z.string().min(1, "Subject Code is required"),
  title: z.string().min(1, "Subject Title is required"),
  units: z.number().int().min(1, "Units must be at least 1"),
});

export type CreateSubjectInput = z.infer<typeof CreateSubjectDto>;
