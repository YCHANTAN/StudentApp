import type { StudentProfileRepository } from "@/application/repositories/student-profile.repository";
import type { EnrollmentRepository } from "@/application/repositories/enrollment.repository";
import type { CourseRepository } from "@/application/repositories/course.repository";
import type { StudentRepository } from "@/application/repositories/student.repository";
import type { TransactionRepository } from "@/application/repositories/transaction.repository";
import type { Assessment } from "@/application/dtos/finance.dto";

export class GetAssessmentUseCase {
  constructor(
    private readonly studentRepo: StudentRepository,
    private readonly studentProfileRepo: StudentProfileRepository,
    private readonly enrollmentRepo: EnrollmentRepository,
    private readonly courseRepo: CourseRepository,
    private readonly transactionRepo: TransactionRepository
  ) {}

  async execute(studentId: string): Promise<Assessment> {
    let internalId = studentId;
    let displayStudentId = studentId;

    const studentAuth = await this.studentRepo.findByStudentId(studentId);
    if (studentAuth) {
        internalId = studentAuth.id;
        displayStudentId = studentAuth.studentId;
    }

    const profile = await this.studentProfileRepo.findById(internalId);
    if (!profile) throw new Error("Student profile not found");

    const enrollmentsResult = await this.enrollmentRepo.findAll({ page: 1, limit: 100 }, { studentId: internalId });
    const activeEnrollments = enrollmentsResult.data.filter(e => e.status !== 'REJECTED');
    
    if (activeEnrollments.length === 0) throw new Error("No active enrollment found for student");

    const allCourseIds = [...new Set(activeEnrollments.flatMap(e => e.courseIds))];
    const courses = await this.courseRepo.findByIds(allCourseIds);
    
    const totalTuition = activeEnrollments.reduce((sum, e) => sum + e.totalTuition, 0);
    const totalUnits = courses.reduce((sum, c) => sum + (c.units || 0), 0);

    const transactions = await this.transactionRepo.findByStudentId(internalId);

    const miscellaneousFees = transactions
        .filter(t => t.type === 'FEE' && !t.title.includes('Tuition Fee')) 
        .map(t => ({
            description: t.title,
            amount: parseFloat(t.amount)
        }));

    const totalMiscellaneous = miscellaneousFees.reduce((sum, f) => sum + f.amount, 0);

    return {
      studentName: profile.fullName,
      studentId: displayStudentId,
      program: profile.programSummary.split(' • ')[0] || "General Program",
      semester: "Spring Semester",
      schoolYear: "2024-2025",
      subjects: courses.map(c => ({
        code: c.code,
        title: c.title,
        units: c.units || 0,
        tuition: c.tuition || 0,
      })),
      totalUnits,
      totalTuition,
      miscellaneousFees,
      totalAssessment: totalTuition + totalMiscellaneous,
    };
  }
}
