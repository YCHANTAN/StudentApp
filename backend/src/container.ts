import { LoginUseCase } from "@/application/use-cases/auth/login.use-case";
import { GetStudentProfileUseCase } from "@/application/use-cases/student-profile/get-student-profile.use-case";
import { UpdateStudentProfileUseCase } from "@/application/use-cases/student-profile/update-student-profile.use-case";
import { GetLibraryBooksUseCase } from "@/application/use-cases/library-book/get-library-books.use-case";
import { UpdateLibraryBookUseCase } from "@/application/use-cases/library-book/update-library-book.use-case";
import { GetDocumentRequestsUseCase } from "@/application/use-cases/document-request/get-document-requests.use-case";
import { GetDocumentRequestUseCase } from "@/application/use-cases/document-request/get-document-request.use-case";
import { CreateDocumentRequestUseCase } from "@/application/use-cases/document-request/create-document-request.use-case";
import { GetComplaintsUseCase } from "@/application/use-cases/complaint/get-complaints.use-case";
import { CreateComplaintUseCase } from "@/application/use-cases/complaint/create-complaint.use-case";
import { GetStudentBalanceUseCase } from "@/application/use-cases/finance/get-balance.use-case";
import { ProcessTransactionUseCase } from "@/application/use-cases/finance/process-transaction.use-case";
import { GetProgramsUseCase } from "@/application/use-cases/program/get-programs.use-case";
import { GetProgramUseCase } from "@/application/use-cases/program/get-program.use-case";
import { GetCoursesUseCase } from "@/application/use-cases/course/get-courses.use-case";
import { GetScheduleEntriesUseCase } from "@/application/use-cases/schedule-entry/get-schedule-entries.use-case";
import { GetGradeRecordsUseCase } from "@/application/use-cases/grade-record/get-grade-records.use-case";
import { GetTransactionsUseCase } from "@/application/use-cases/transaction/get-transactions.use-case";
import { GetEnrollmentsUseCase } from "@/application/use-cases/enrollment/get-enrollments.use-case";
import { CreateEnrollmentUseCase } from "@/application/use-cases/enrollment/create-enrollment.use-case";
import { UpdateEnrollmentUseCase } from "@/application/use-cases/enrollment/update-enrollment.use-case";
import { DeleteEnrollmentUseCase } from "@/application/use-cases/enrollment/delete-enrollment.use-case";
import { GetStudentSubjectsUseCase } from "@/application/use-cases/registration/get-student-subjects.use-case";
import { CreateSubjectUseCase } from "@/application/use-cases/subject/create-subject.use-case";

import { db } from "@/infrastructure/db/client";
import { StudentProfilePgRepository } from "@/infrastructure/db/repositories/student-profile.pg.repository";
import { StudentPgRepository } from "@/infrastructure/db/repositories/student.pg.repository";
import { LibraryBookPgRepository } from "@/infrastructure/db/repositories/library-book.pg.repository";
import { DocumentRequestPgRepository } from "@/infrastructure/db/repositories/document-request.pg.repository";
import { ComplaintPgRepository } from "@/infrastructure/db/repositories/complaint.pg.repository";
import { TransactionPgRepository } from "@/infrastructure/db/repositories/transaction.pg.repository";
import { ProgramPgRepository } from "@/infrastructure/db/repositories/program.pg.repository";
import { CoursePgRepository } from "@/infrastructure/db/repositories/course.pg.repository";
import { ScheduleEntryPgRepository } from "@/infrastructure/db/repositories/schedule-entry.pg.repository";
import { GradeRecordPgRepository } from "@/infrastructure/db/repositories/grade-record.pg.repository";
import { EnrollmentPgRepository } from "@/infrastructure/db/repositories/enrollment.pg.repository";
import { SubjectRegistrationPgRepository } from "@/infrastructure/db/repositories/subject-registration.pg.repository";
import { SubjectPgRepository } from "@/infrastructure/db/repositories/subject.pg.repository";

import { AuthController } from "@/presentation/controllers/auth.controller";
import { StudentProfileController } from "@/presentation/controllers/student-profile.controller";
import { LibraryBookController } from "@/presentation/controllers/library-book.controller";
import { DocumentRequestController } from "@/presentation/controllers/document-request.controller";
import { ComplaintController } from "@/presentation/controllers/complaint.controller";
import { FinanceController } from "@/presentation/controllers/finance.controller";
import { ProgramController } from "@/presentation/controllers/program.controller";
import { CourseController } from "@/presentation/controllers/course.controller";
import { ScheduleEntryController } from "@/presentation/controllers/schedule-entry.controller";
import { GradeRecordController } from "@/presentation/controllers/grade-record.controller";
import { TransactionController } from "@/presentation/controllers/transaction.controller";
import { EnrollmentController } from "@/presentation/controllers/enrollment.controller";
import { SubjectRegistrationController } from "@/presentation/controllers/subject-registration.controller";
import { SubjectController } from "@/presentation/controllers/subject.controller";

// --- Repositories ---
const studentRepo = new StudentPgRepository(db);
const studentProfileRepo = new StudentProfilePgRepository(db);
const libraryBookRepo = new LibraryBookPgRepository(db);
const documentRequestRepo = new DocumentRequestPgRepository(db);
const complaintRepo = new ComplaintPgRepository(db);
const transactionRepo = new TransactionPgRepository(db);
const programRepo = new ProgramPgRepository(db);
const courseRepo = new CoursePgRepository(db);
const scheduleEntryRepo = new ScheduleEntryPgRepository(db);
const gradeRecordRepo = new GradeRecordPgRepository(db);
const enrollmentRepo = new EnrollmentPgRepository(db);
const registrationRepo = new SubjectRegistrationPgRepository(db);
const subjectRepo = new SubjectPgRepository(db);

// --- Use Cases ---
const loginUseCase = new LoginUseCase(studentRepo);
const getStudentProfileUseCase = new GetStudentProfileUseCase(studentProfileRepo);
const updateStudentProfileUseCase = new UpdateStudentProfileUseCase(studentProfileRepo);

const getLibraryBooksUseCase = new GetLibraryBooksUseCase(libraryBookRepo);
const updateLibraryBookUseCase = new UpdateLibraryBookUseCase(libraryBookRepo);

const getDocumentRequestsUseCase = new GetDocumentRequestsUseCase(documentRequestRepo, studentRepo);
const getDocumentRequestUseCase = new GetDocumentRequestUseCase(documentRequestRepo);
const createDocumentRequestUseCase = new CreateDocumentRequestUseCase(documentRequestRepo);

const getComplaintsUseCase = new GetComplaintsUseCase(complaintRepo, studentRepo);
const createComplaintUseCase = new CreateComplaintUseCase(complaintRepo);

const getStudentBalanceUseCase = new GetStudentBalanceUseCase(transactionRepo);
const processTransactionUseCase = new ProcessTransactionUseCase(transactionRepo);

const getProgramsUseCase = new GetProgramsUseCase(programRepo);
const getProgramUseCase = new GetProgramUseCase(programRepo);

const getCoursesUseCase = new GetCoursesUseCase(courseRepo);

const getScheduleEntriesUseCase = new GetScheduleEntriesUseCase(scheduleEntryRepo, studentRepo);

const getGradeRecordsUseCase = new GetGradeRecordsUseCase(gradeRecordRepo, studentRepo);

const getTransactionsUseCase = new GetTransactionsUseCase(transactionRepo, studentRepo);

const getEnrollmentsUseCase = new GetEnrollmentsUseCase(enrollmentRepo, studentRepo);
const createEnrollmentUseCase = new CreateEnrollmentUseCase(enrollmentRepo, courseRepo);
const updateEnrollmentUseCase = new UpdateEnrollmentUseCase(enrollmentRepo);
const deleteEnrollmentUseCase = new DeleteEnrollmentUseCase(enrollmentRepo);

const getStudentSubjectsUseCase = new GetStudentSubjectsUseCase(registrationRepo);
const createSubjectUseCase = new CreateSubjectUseCase(subjectRepo);

// --- Controllers ---
export const authController = new AuthController(loginUseCase);

export const studentProfileController = new StudentProfileController(
  getStudentProfileUseCase,
  updateStudentProfileUseCase,
);

export const libraryBookController = new LibraryBookController(
  getLibraryBooksUseCase,
  updateLibraryBookUseCase,
);

export const documentRequestController = new DocumentRequestController(
  getDocumentRequestsUseCase,
  getDocumentRequestUseCase,
  createDocumentRequestUseCase
);

export const complaintController = new ComplaintController(
  getComplaintsUseCase,
  createComplaintUseCase
);

export const financeController = new FinanceController(
  getStudentBalanceUseCase,
  processTransactionUseCase,
  transactionRepo
);

export const programController = new ProgramController(
  getProgramsUseCase,
  getProgramUseCase
);

export const courseController = new CourseController(getCoursesUseCase);

export const scheduleEntryController = new ScheduleEntryController(getScheduleEntriesUseCase);

export const gradeRecordController = new GradeRecordController(getGradeRecordsUseCase);

export const transactionController = new TransactionController(getTransactionsUseCase);

export const enrollmentController = new EnrollmentController(
  getEnrollmentsUseCase,
  createEnrollmentUseCase,
  updateEnrollmentUseCase,
  deleteEnrollmentUseCase
);

export const subjectRegistrationController = new SubjectRegistrationController(
  getStudentSubjectsUseCase
);

export const subjectController = new SubjectController(createSubjectUseCase);
