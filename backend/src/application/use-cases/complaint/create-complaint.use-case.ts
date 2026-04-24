import type { ComplaintRepository } from '@/application/repositories/complaint.repository';
import type { CreateComplaintInput } from '@/application/dtos/complaint.dto';
import type { Complaint } from '@/core/entities/complaint.entity';

export class CreateComplaintUseCase {
  constructor(private readonly complaintRepo: ComplaintRepository) {}

  async execute(input: CreateComplaintInput): Promise<Complaint> {
    const complaint: Complaint = {
      id: crypto.randomUUID(),
      studentId: input.studentId,
      title: input.title,
      status: 'IN_REVIEW',
      createdAt: new Date(),
      updatedAt: new Date(),
    };

    return this.complaintRepo.save(complaint);
  }
}
