// src/application/use-cases/subject/create-subject.use-case.ts
import type { SubjectRepository, Subject } from '@/application/repositories/subject.repository';
import type { CreateSubjectInput } from '@/application/dtos/subject.dto';

export class CreateSubjectUseCase {
  constructor(private readonly subjectRepo: SubjectRepository) {}

  async execute(input: CreateSubjectInput): Promise<Subject> {
    const subject: Subject = {
      id: input.id,
      title: input.title,
      units: input.units,
    };

    return this.subjectRepo.save(subject);
  }
}
