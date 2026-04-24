import type { DocumentRequest } from '@/core/entities/document-request.entity';

export interface DocumentRequestRepository {
  findAll(pagination: { page: number; limit: number }, filter?: { studentId?: string }): Promise<{ data: DocumentRequest[]; total: number }>;
  findById(id: string): Promise<DocumentRequest | null>;
  save(request: DocumentRequest): Promise<DocumentRequest>;
}
