import { z } from 'zod';

export const GradeRecordStatusDto = z.enum(['COMPLETED', 'IN_PROGRESS']);

export const CreateGradeRecordDto = z.object({
  studentId: z.string(),
  title: z.string(),
  codeCredits: z.string(),
  gradePoint: z.string(),
  status: GradeRecordStatusDto,
  semesterLabel: z.string().optional(),
});

export const UpdateGradeRecordDto = CreateGradeRecordDto.partial();

export const GradeRecordResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  title: z.string(),
  codeCredits: z.string(),
  gradePoint: z.string(),
  status: GradeRecordStatusDto,
  semesterLabel: z.string().optional(),
  createdAt: z.string().datetime(),
});

export type CreateGradeRecordInput = z.infer<typeof CreateGradeRecordDto>;
export type UpdateGradeRecordInput = z.infer<typeof UpdateGradeRecordDto>;

export const GetGradeRecordsQueryDto = z.object({
  studentId: z.string().optional(),
});
