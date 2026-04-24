import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import { NotFoundError } from '@/core/errors/domain.error';

export class GetDocumentRequestUseCase {
  constructor(private readonly documentRequestRepo: DocumentRequestRepository) {}

  async execute(id: string) {
    const request = await this.documentRequestRepo.findById(id);
    if (!request) throw new NotFoundError('DocumentRequest');
    return request;
  }
}
