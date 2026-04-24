import type { ScheduleEntry } from '@/core/entities/schedule-entry.entity';

export interface ScheduleEntryRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { studentId?: string }): Promise<{ data: ScheduleEntry[]; total: number }>;
}
