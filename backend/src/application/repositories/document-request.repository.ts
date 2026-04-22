import type { DocumentRequest } from '@/core/entities/document-request.entity';

export interface DocumentRequestRepository {
  save(request: DocumentRequest): Promise<DocumentRequest>;
  findById(id: string): Promise<DocumentRequest | null>;
  findByStudentId(studentId: string): Promise<DocumentRequest[]>;
}