// src/application/repositories/subject.repository.ts

export type Subject = {
  id: string;
  title: string;
  units: number;
};

export interface SubjectRepository {
  findByIds(ids: string[]): Promise<Subject[]>;
  save(subject: Subject): Promise<Subject>;
}
