import { z } from 'zod';

export const CourseStatusDto = z.enum(['Enrolled', 'Completed', 'Waitlisted']);

export const CreateCourseDto = z.object({
  code: z.string(),
  title: z.string(),
  semesterTitle: z.string().optional(),
  instructor: z.string().optional(),
  units: z.number().int().optional(),
  schedule: z.string().optional(),
  location: z.string().optional(),
  grade: z.string().optional(),
  waitlistStatus: z.string().optional(),
  progress: z.number().min(0).max(1).optional(),
  status: CourseStatusDto.optional(),
  tuition: z.number().optional(),
  remainingSlots: z.number().int().optional(),
  isLocked: z.boolean().optional(),
  lockReason: z.string().optional(),
  programId: z.string().uuid().optional(),
});

export const UpdateCourseDto = CreateCourseDto.partial();

export const CourseResponseDto = z.object({
  id: z.string().uuid(),
  code: z.string(),
  title: z.string(),
  semesterTitle: z.string().optional(),
  instructor: z.string().optional(),
  units: z.number().int().optional(),
  schedule: z.string().optional(),
  location: z.string().optional(),
  grade: z.string().optional(),
  waitlistStatus: z.string().optional(),
  progress: z.number().optional(),
  status: CourseStatusDto.optional(),
  tuition: z.number().optional(),
  remainingSlots: z.number().int().optional(),
  isLocked: z.boolean().optional(),
  lockReason: z.string().optional(),
  programId: z.string().uuid().optional(),
  createdAt: z.string().datetime(),
});

export type CreateCourseInput = z.infer<typeof CreateCourseDto>;
export type UpdateCourseInput = z.infer<typeof UpdateCourseDto>;

export const GetCoursesQueryDto = z.object({
  programId: z.string().uuid().optional(),
  code: z.string().optional(),
});
