import { z } from 'zod';

export const ProgramCategoryDto = z.enum(['Undergraduate', 'Postgraduate']);
export const BadgeVariantDto = z.enum(['Accent', 'Primary']);

export const CreateProgramDto = z.object({
  title: z.string(),
  badgeText: z.string(),
  badgeVariant: BadgeVariantDto,
  scheduleLine: z.string(),
  description: z.string(),
  category: ProgramCategoryDto,
});

export const UpdateProgramDto = CreateProgramDto.partial();

export const ProgramResponseDto = z.object({
  id: z.string().uuid(),
  title: z.string(),
  badgeText: z.string(),
  badgeVariant: BadgeVariantDto,
  scheduleLine: z.string(),
  description: z.string(),
  category: ProgramCategoryDto,
  createdAt: z.string().datetime(),
});

export type CreateProgramInput = z.infer<typeof CreateProgramDto>;
export type UpdateProgramInput = z.infer<typeof UpdateProgramDto>;
export type ProgramResponse = z.infer<typeof ProgramResponseDto>;

export const GetProgramsQueryDto = z.object({
  category: ProgramCategoryDto.optional(),
});
