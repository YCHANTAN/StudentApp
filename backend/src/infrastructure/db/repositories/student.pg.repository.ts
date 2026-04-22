import { eq } from "drizzle-orm";
import type { NodePgDatabase } from "drizzle-orm/node-postgres";

import type { StudentAuthRecord, StudentRepository } from "@/application/repositories/student.repository";
import type { Student } from "@/core/entities/student.entity";
import { type StudentRow, students } from "@/infrastructure/db/schema/student.schema";

export class StudentPgRepository implements StudentRepository {
  constructor(private readonly db: NodePgDatabase) {}

  private toStudent(row: StudentRow): Student {
    return {
      id: row.id,
      studentId: row.studentId,
      fullName: row.fullName,
      email: row.email,
      createdAt: row.createdAt,
      updatedAt: row.updatedAt,
    };
  }

  async findById(id: string): Promise<Student | null> {
    const [row] = await this.db.select().from(students).where(eq(students.id, id));
    return row ? this.toStudent(row) : null;
  }

  async findByStudentId(studentId: string): Promise<Student | null> {
    const [row] = await this.db.select().from(students).where(eq(students.studentId, studentId));
    return row ? this.toStudent(row) : null;
  }

  async findAuthByStudentId(studentId: string): Promise<StudentAuthRecord | null> {
    const [row] = await this.db.select().from(students).where(eq(students.studentId, studentId));
    if (!row) {
      return null;
    }

    return {
      id: row.id,
      studentId: row.studentId,
      passwordHash: row.passwordHash,
    };
  }

  async findByEmail(email: string): Promise<Student | null> {
    const [row] = await this.db.select().from(students).where(eq(students.email, email));
    return row ? this.toStudent(row) : null;
  }

  async findAll(): Promise<Student[]> {
    const rows = await this.db.select().from(students);
    return rows.map((row) => this.toStudent(row));
  }

  async save(student: Student): Promise<Student> {
    const [row] = await this.db.insert(students).values(student).returning();
    if (!row) {
      throw new Error("Failed to persist student");
    }
    return this.toStudent(row);
  }

  async update(id: string, data: Partial<Student>): Promise<Student> {
    const [row] = await this.db.update(students).set(data).where(eq(students.id, id)).returning();
    if (!row) {
      throw new Error("Failed to update student");
    }
    return this.toStudent(row);
  }

  async delete(id: string): Promise<void> {
    await this.db.delete(students).where(eq(students.id, id));
  }
}
