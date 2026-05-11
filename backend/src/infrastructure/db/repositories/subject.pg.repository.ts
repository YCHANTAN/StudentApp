// src/infrastructure/db/repositories/subject.pg.repository.ts
import { inArray } from 'drizzle-orm';
import type { NodePgDatabase } from 'drizzle-orm/node-postgres';
import type { SubjectRepository, Subject } from '@/application/repositories/subject.repository';
import { subjects } from '../schema/subject-registration.schema';

export class SubjectPgRepository implements SubjectRepository {
  constructor(private readonly db: NodePgDatabase<any>) {}

  async findByIds(ids: string[]): Promise<Subject[]> {
    if (ids.length === 0) return [];
    return this.db.select().from(subjects).where(inArray(subjects.id, ids));
  }

  async save(subject: Subject): Promise<Subject> {
    const [row] = await this.db.insert(subjects).values(subject).returning();
    if (!row) throw new Error('Failed to save subject');
    return row;
  }
}
