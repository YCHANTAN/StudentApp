import { eq } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import type { DocumentRequest } from '@/core/entities/document-request.entity';
import { documentRequests } from '../schema/document-request.schema.js';

export class DocumentRequestPgRepository implements DocumentRequestRepository {
  constructor(private readonly db: NodePgDatabase<Record<string, never>>) {}

  async save(request: DocumentRequest): Promise<DocumentRequest> {
    // Insert the record into the database and return the newly created row
    const [row] = await this.db.insert(documentRequests).values(request).returning();
    if (!row) throw new Error('Failed to persist document request');
    
    // Cast it back to the pure entity to satisfy TypeScript
    return row as DocumentRequest;
  }

  async findById(id: string): Promise<DocumentRequest | null> {
    const [row] = await this.db.select().from(documentRequests).where(eq(documentRequests.id, id));
    return (row as DocumentRequest) ?? null;
  }

  async findByStudentId(studentId: string): Promise<DocumentRequest[]> {
    const rows = await this.db.select().from(documentRequests).where(eq(documentRequests.studentId, studentId));
    return rows as DocumentRequest[];
  }
}