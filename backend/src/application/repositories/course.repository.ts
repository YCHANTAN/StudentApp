import type { Course } from '@/core/entities/course.entity';

export interface CourseRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { programId?: string; code?: string }): Promise<{ data: Course[]; total: number }>;
  findById(id: string): Promise<Course | null>;
  findByIds(ids: string[]): Promise<Course[]>;
}
