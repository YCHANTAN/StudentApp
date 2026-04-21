import { z } from 'zod';

export const GetProgramsQueryDto = z.object({
  category: z.enum(['Undergraduate', 'Postgraduate']).optional(),
});