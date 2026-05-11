import PDFDocument from 'pdfkit';
import type { EnrollmentRepository } from '@/application/repositories/enrollment.repository';
import type { StudentProfileRepository } from '@/application/repositories/student-profile.repository';
import type { CourseRepository } from '@/application/repositories/course.repository';

export class GetStudyLoadPdfUseCase {
  constructor(
    private readonly enrollmentRepository: EnrollmentRepository,
    private readonly studentProfileRepository: StudentProfileRepository,
    private readonly courseRepository: CourseRepository
  ) {}

  async execute(studentId: string): Promise<Buffer> {
    const profile = await this.studentProfileRepository.findById(studentId);
    if (!profile) throw new Error('Student profile not found');

    const enrollments = await this.enrollmentRepository.findAll({ page: 1, limit: 10 }, { studentId });
    // Filter out rejected and pick the most recent
    const activeEnrollment = enrollments.data
      .filter(e => e.status !== 'REJECTED')
      .sort((a, b) => b.createdAt.getTime() - a.createdAt.getTime())[0];

    if (!activeEnrollment) throw new Error('No active enrollment found');

    const allCourses = await this.courseRepository.findByIds(activeEnrollment.courseIds);

    // Ensure unique courses by code
    const uniqueCourses: typeof allCourses = [];
    const seenCodes = new Set<string>();
    for (const course of allCourses) {
      if (!seenCodes.has(course.code)) {
        uniqueCourses.push(course);
        seenCodes.add(course.code);
      }
    }

    return new Promise((resolve, reject) => {
      const doc = new PDFDocument({ margin: 50 });
      const chunks: Buffer[] = [];

      doc.on('data', (chunk) => chunks.push(chunk));
      doc.on('end', () => resolve(Buffer.concat(chunks)));
      doc.on('error', reject);

      // Header
      doc.fontSize(20).text('OFFICIAL STUDY LOAD', { align: 'center' });
      doc.moveDown();

      // Student Info Section
      doc.fontSize(12).font('Helvetica-Bold').text('STUDENT INFORMATION');
      doc.font('Helvetica').fontSize(10);
      doc.text(`Name: ${profile.fullName}`);
      doc.text(`Student ID: ${profile.id}`);
      doc.text(`Program/Year: ${profile.programSummary}`);
      doc.moveDown();

      // Enrollment Details
      doc.fontSize(12).font('Helvetica-Bold').text('ENROLLED SUBJECTS');
      doc.moveDown(0.5);

      // Table Header
      const tableTop = doc.y;
      doc.fontSize(10);
      doc.text('Code', 50, tableTop);
      doc.text('Subject Title', 100, tableTop);
      doc.text('Units', 350, tableTop);
      doc.text('Schedule', 400, tableTop);
      
      doc.moveTo(50, tableTop + 15).lineTo(550, tableTop + 15).stroke();
      doc.moveDown(1);

      // Table Body
      uniqueCourses.forEach((course) => {
        const y = doc.y;
        doc.text(course.code, 50, y);
        doc.text(course.title, 100, y, { width: 240 });
        doc.text((course.units ?? 0).toString(), 350, y);
        doc.text(course.schedule || 'TBA', 400, y, { width: 150 });
        doc.moveDown(1.5);
      });

      doc.moveTo(50, doc.y).lineTo(550, doc.y).stroke();
      doc.moveDown(1);

      // Summary
      doc.fontSize(11).font('Helvetica-Bold');
      const totalUnits = uniqueCourses.reduce((sum, c) => sum + (c.units ?? 0), 0);
      doc.text(`Total Units: ${totalUnits}`, { align: 'right' });
      doc.text(`Semester: ${uniqueCourses[0]?.semesterTitle || 'Current Semester'}`, { align: 'right' });

      // Footer
      doc.fontSize(8).font('Helvetica-Oblique')
        .text(`Generated on ${new Date().toLocaleString()}`, 50, doc.page.height - 50, { align: 'center' });

      doc.end();
    });
  }
}
