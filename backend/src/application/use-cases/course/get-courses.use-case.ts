import type { CourseRepository } from '@/application/repositories/course.repository';

export type GetCoursesInput = {
  page: number;
  limit: number;
  programId?: string;
  code?: string;
};

export class GetCoursesUseCase {
  constructor(private readonly courseRepo: CourseRepository) {}

  async execute(input: GetCoursesInput) {
    const { page, limit, programId, code } = input;
    const filter: { programId?: string; code?: string } = {};
    if (programId !== undefined) filter.programId = programId;
    if (code !== undefined) filter.code = code;

    return this.courseRepo.findAll({ page, limit }, filter);
  }
}
