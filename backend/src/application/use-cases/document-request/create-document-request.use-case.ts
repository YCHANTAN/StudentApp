import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import type { CreateDocumentRequestInput } from '@/application/dtos/document-request.dto';
import type { DocumentRequest } from '@/core/entities/document-request.entity';

export class CreateDocumentRequestUseCase {
  constructor(private readonly documentRequestRepo: DocumentRequestRepository) {}

  async execute(input: CreateDocumentRequestInput): Promise<DocumentRequest> {
    const request: DocumentRequest = {
      id: crypto.randomUUID(),
      studentId: input.studentId,
      type: input.type,
      purpose: input.purpose,
      program: input.program,
      yearLevel: input.yearLevel,
      copies: input.copies,
      deliveryMethod: input.deliveryMethod,
      reference: `REF-${Math.random().toString(36).substring(2, 9).toUpperCase()}`,
      status: 'PROCESSING',
      submittedAt: new Date(),
      updatedAt: new Date(),
    };

    return this.documentRequestRepo.save(request);
  }
}
