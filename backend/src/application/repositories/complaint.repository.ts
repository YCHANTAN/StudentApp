import type { Complaint } from '@/core/entities/complaint.entity';

export interface ComplaintRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { studentId?: string }): Promise<{ data: Complaint[]; total: number }>;
  save(complaint: Complaint): Promise<Complaint>;
}
