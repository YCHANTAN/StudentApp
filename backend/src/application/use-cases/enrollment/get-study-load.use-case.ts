import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { CourseRepository } from '@/application/repositories/course.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { Course } from '@/core/entities/course.entity';

export type StudyLoadResponse = {
  courses: Course[];
  totalUnits: number;
  semesterLabel: string;
};

export class GetStudyLoadUseCase {
  constructor(
    private readonly enrollmentRepository: EnrollmentRepository,
    private readonly courseRepository: CourseRepository,
    private readonly studentRepository: StudentRepository
  ) {}

  async execute(studentId: string): Promise<StudyLoadResponse> {
    let targetId = studentId;

    // Resolve STU-ID to database UUID if necessary
    if (!this.isUuid(studentId)) {
      const student = await this.studentRepository.findByStudentId(studentId);
      if (student) {
        targetId = student.id;
      }
    }

    // 1. Fetch all enrollments for the student
    const enrollments = await this.enrollmentRepository.findAll({ page: 1, limit: 100 }, { studentId: targetId });
    
    // 2. Filter for active (not rejected) enrollments
    const activeEnrollments = enrollments.data.filter(e => e.status !== 'REJECTED');
    
    if (activeEnrollments.length === 0) {
      return {
        courses: [],
        totalUnits: 0,
        semesterLabel: 'No Active Enrollment',
      };
    }

    // 3. Get all unique course IDs from active enrollments
    const allCourseIds = [...new Set(activeEnrollments.flatMap(e => e.courseIds))];
    
    // 4. Fetch the full course details
    const allCourses = await this.courseRepository.findByIds(allCourseIds);

    // 5. Ensure unique courses by code to prevent duplicates
    const uniqueCourses: Course[] = [];
    const seenCodes = new Set<string>();
    
    for (const course of allCourses) {
      if (!seenCodes.has(course.code)) {
        uniqueCourses.push(course);
        seenCodes.add(course.code);
      }
    }

    // 6. Calculate total units and get a representative semester label
    const totalUnits = uniqueCourses.reduce((sum, c) => sum + (c.units || 0), 0);
    
    const semesterLabel = uniqueCourses.find(c => c.semesterTitle)?.semesterTitle || 'Current Semester';

    return {
      courses: uniqueCourses,
      totalUnits,
      semesterLabel,
    };
  }

  private isUuid(id: string): boolean {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(id);
  }
}
