import type { CourseRepository } from '@/application/repositories/course.repository';

export type GetCoursesInput = {
  page: number;
  limit: number;
  programId?: string;
};

export class GetCoursesUseCase {
  constructor(private readonly courseRepo: CourseRepository) {}

  async execute(input: GetCoursesInput) {
    const { page, limit, programId } = input;
    const filter: { programId?: string } = {};
    if (programId !== undefined) filter.programId = programId;

    return this.courseRepo.findAll({ page, limit }, filter);
  }
}
