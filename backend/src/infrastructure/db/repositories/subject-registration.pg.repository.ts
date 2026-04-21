import { eq, and } from 'drizzle-orm';
import { db } from '../client';
import { subjects, sections, subjectRegistrations } from '../schema/subject-registration.schema';

export class SubjectRegistrationPgRepository {
  constructor(private readonly database: typeof db) {}

  async findSubjectsByStudentAndStatus(studentId: string, status: 'Enrolled' | 'Completed' | 'Waitlisted') {
    return this.database
      .select({
        registrationId: subjectRegistrations.id,
        subjectCode: subjects.id,
        subjectTitle: subjects.title,
        units: subjects.units,
        instructor: sections.instructorName,
        schedule: sections.timeSlots,
        location: sections.location,
        progressPercentage: subjectRegistrations.progressPercentage,
      })
      .from(subjectRegistrations)
      .innerJoin(sections, eq(subjectRegistrations.sectionId, sections.id))
      .innerJoin(subjects, eq(sections.subjectId, subjects.id))
      .where(
        and(
          eq(subjectRegistrations.studentId, studentId),
          eq(subjectRegistrations.status, status)
        )
      );
  }
}