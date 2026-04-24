import { z } from 'zod';

export const ComplaintStatusDto = z.enum(['IN_REVIEW', 'RESOLVED']);

export const CreateComplaintDto = z.object({
  studentId: z.string(),
  title: z.string().min(1),
});

export const ComplaintResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  title: z.string(),
  status: ComplaintStatusDto,
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type CreateComplaintInput = z.infer<typeof CreateComplaintDto>;

export const GetComplaintsQueryDto = z.object({
  studentId: z.string().optional(),
});
