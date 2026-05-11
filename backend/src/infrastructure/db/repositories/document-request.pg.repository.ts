import { eq, count } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import type { DocumentRequest, DocumentType, DeliveryMethod, DocumentRequestStatus } from '@/core/entities/document-request.entity';
import { documentRequests } from '../schema/document-request.schema';

export class DocumentRequestPgRepository implements DocumentRequestRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findAll(
    pagination: { page: number; limit: number },
    filter?: { studentId?: string }
  ): Promise<{ data: DocumentRequest[]; total: number }> {
    const { page, limit } = pagination;
    const offset = (page - 1) * limit;

    const whereClause = filter?.studentId ? eq(documentRequests.studentId, filter.studentId) : undefined;

    const data = await this.db
      .select()
      .from(documentRequests)
      .where(whereClause)
      .limit(limit)
      .offset(offset);

    const [totalResult] = await this.db.select({ value: count() }).from(documentRequests).where(whereClause);

    const total = totalResult?.value ? Number(totalResult.value) : 0;

    return {
      data: data.map(this.mapToEntity),
      total,
    };
  }

  async findById(id: string): Promise<DocumentRequest | null> {
    const [row] = await this.db.select().from(documentRequests).where(eq(documentRequests.id, id));
    return row ? this.mapToEntity(row) : null;
  }

  async save(request: DocumentRequest): Promise<DocumentRequest> {
    const [row] = await this.db.insert(documentRequests).values(request).returning();
    if (!row) throw new Error('Failed to save document request');
    return this.mapToEntity(row);
  }

  private mapToEntity(row: any): DocumentRequest {
    return {
      id: row.id,
      studentId: row.studentId,
      type: row.type as DocumentType,
      purpose: row.purpose,
      program: row.program || undefined,
      yearLevel: row.yearLevel || undefined,
      copies: row.copies || undefined,
      deliveryMethod: row.deliveryMethod as DeliveryMethod || undefined,
      reference: row.reference,
      status: row.status as DocumentRequestStatus,
      submittedAt: row.submittedAt,
      updatedAt: row.updatedAt,
    };
  }
}

