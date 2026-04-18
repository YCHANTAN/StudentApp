import { eq } from "drizzle-orm";
import type { NodePgDatabase } from "drizzle-orm/node-postgres";

import type { StudentRepository } from "@/application/repositories/student.repository";
import type { Student } from "@/core/entities/student.entity";
import { students } from "@/infrastructure/db/schema/student.schema";

export class StudentPgRepository implements StudentRepository {
  constructor(private readonly db: NodePgDatabase) {}

  async findById(id: string): Promise<Student | null> {
    const [row] = await this.db.select().from(students).where(eq(students.id, id));
    return row ?? null;
  }

  async findByStudentId(studentId: string): Promise<Student | null> {
    const [row] = await this.db.select().from(students).where(eq(students.studentId, studentId));
    return row ?? null;
  }

  async findByEmail(email: string): Promise<Student | null> {
    const [row] = await this.db.select().from(students).where(eq(students.email, email));
    return row ?? null;
  }

  async findAll(): Promise<Student[]> {
    return this.db.select().from(students);
  }

  async save(student: Student): Promise<Student> {
    const [row] = await this.db.insert(students).values(student).returning();
    if (!row) {
      throw new Error("Failed to persist student");
    }
    return row;
  }

  async update(id: string, data: Partial<Student>): Promise<Student> {
    const [row] = await this.db.update(students).set(data).where(eq(students.id, id)).returning();
    if (!row) {
      throw new Error("Failed to update student");
    }
    return row;
  }

  async delete(id: string): Promise<void> {
    await this.db.delete(students).where(eq(students.id, id));
  }
}
