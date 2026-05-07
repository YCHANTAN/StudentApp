import { z } from 'zod';

export const EnrollmentStatusDto = z.enum(['PENDING', 'APPROVED', 'REJECTED']);

export const CreateEnrollmentDto = z.object({
  studentId: z.string(),
  courseIds: z.array(z.string().uuid()).min(1),
});

export const UpdateEnrollmentDto = z.object({
  status: EnrollmentStatusDto.optional(),
  courseIds: z.array(z.string().uuid()).optional(),
});

export const EnrollmentResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  status: EnrollmentStatusDto,
  totalUnits: z.number(),
  totalTuition: z.number(),
  courseIds: z.array(z.string().uuid()),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type CreateEnrollmentInput = z.infer<typeof CreateEnrollmentDto>;
export type UpdateEnrollmentInput = z.infer<typeof UpdateEnrollmentDto>;

export const GetEnrollmentsQueryDto = z.object({
  studentId: z.string().optional(),
});
