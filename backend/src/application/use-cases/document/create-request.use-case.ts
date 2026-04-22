// src/application/use-cases/document/create-request.use-case.ts
import { randomUUID } from 'crypto';
import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import type { CreateDocumentRequestInput } from '@/application/dtos/document-request.dto';
import type { DocumentRequest } from '@/core/entities/document-request.entity';

export class CreateDocumentRequestUseCase {
  constructor(private readonly documentRepo: DocumentRequestRepository) {}

  async execute(input: CreateDocumentRequestInput): Promise<DocumentRequest> {
    const newRequest: DocumentRequest = {
      id: randomUUID(),
      studentId: input.studentId,
      documentType: input.documentType,
      purpose: input.purpose,
      copies: input.copies,
      status: 'PENDING', // Force default state
      notes: input.notes ?? null,
      requestedAt: new Date(),
      updatedAt: new Date(),
    };

    return await this.documentRepo.save(newRequest);
  }
}