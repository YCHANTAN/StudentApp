import { z } from 'zod';

export const CreateScheduleEntryDto = z.object({
  studentId: z.string(),
  dayLabel: z.string(),
  courseCode: z.string(),
  courseTitle: z.string(),
  timeRange: z.string(),
  room: z.string(),
  instructor: z.string(),
});

export const UpdateScheduleEntryDto = CreateScheduleEntryDto.partial();

export const ScheduleEntryResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  dayLabel: z.string(),
  courseCode: z.string(),
  courseTitle: z.string(),
  timeRange: z.string(),
  room: z.string(),
  instructor: z.string(),
  createdAt: z.string().datetime(),
});

export type CreateScheduleEntryInput = z.infer<typeof CreateScheduleEntryDto>;
export type UpdateScheduleEntryInput = z.infer<typeof UpdateScheduleEntryDto>;

export const GetScheduleEntriesQueryDto = z.object({
  studentId: z.string().optional(),
});
