import type { GradeRecord } from '@/core/entities/grade-record.entity';

export interface GradeRecordRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { studentId?: string }): Promise<{ data: GradeRecord[]; total: number }>;
}
