import { eq } from 'drizzle-orm';
import { db } from '../client';
import { studentCredentials } from '../schema/auth.schema';

export class AuthPgRepository {
  constructor(private readonly database: typeof db) {}

  async findCredentialsByStudentId(studentId: string) {
    const result = await this.database.select().from(studentCredentials).where(eq(studentCredentials.studentId, studentId));
    return result[0] || null;
  }

  async saveCredentials(studentId: string, passwordHash: string) {
    return this.database.insert(studentCredentials).values({
      studentId,
      passwordHash,
      requiresPasswordChange: true
    }).returning();
  }
}