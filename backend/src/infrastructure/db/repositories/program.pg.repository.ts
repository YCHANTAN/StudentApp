import { eq } from 'drizzle-orm';
import { db } from '../client';
import { programs } from '../schema/program.schema';

export class ProgramPgRepository {
  constructor(private readonly database: typeof db) {}

  async findAll(category?: 'Undergraduate' | 'Postgraduate') {
    if (category) {
      return this.database.select().from(programs).where(eq(programs.category, category));
    }
    return this.database.select().from(programs);
  }

  async findById(id: string) {
    const result = await this.database.select().from(programs).where(eq(programs.id, id));
    return result[0] || null;
  }
}