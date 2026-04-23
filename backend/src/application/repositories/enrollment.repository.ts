// src/application/repositories/enrollment.repository.ts
import type { Enrollment } from '@/core/entities/enrollment.entity';

export interface EnrollmentRepository {
  findById(id: string): Promise<Enrollment | null>;
  findAll(): Promise<Enrollment[]>;
  save(enrollment: Enrollment): Promise<Enrollment>;
  update(id: string, data: Partial<Enrollment>): Promise<Enrollment>;
  delete(id: string): Promise<void>;
}
