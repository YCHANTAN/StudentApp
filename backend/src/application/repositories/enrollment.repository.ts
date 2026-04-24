import type { Enrollment } from '@/core/entities/enrollment.entity';

export interface EnrollmentRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { studentId?: string }): Promise<{ data: Enrollment[]; total: number }>;
  findById(id: string): Promise<Enrollment | null>;
  save(enrollment: Enrollment): Promise<Enrollment>;
  update(id: string, patch: Partial<Enrollment>): Promise<Enrollment>;
  delete(id: string): Promise<void>;
}
