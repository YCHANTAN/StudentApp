import type { Student } from "@/core/entities/student.entity";

export type StudentAuthRecord = {
  id: string;
  studentId: string;
  passwordHash: string | null;
};

export interface StudentRepository {
  findById(id: string): Promise<Student | null>;
  findByStudentId(studentId: string): Promise<Student | null>;
  findAuthByStudentId(studentId: string): Promise<StudentAuthRecord | null>;
  findByEmail(email: string): Promise<Student | null>;
  findAll(): Promise<Student[]>;
  save(student: Student): Promise<Student>;
  update(id: string, data: Partial<Student>): Promise<Student>;
  delete(id: string): Promise<void>;
}
