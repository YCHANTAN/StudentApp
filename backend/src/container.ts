import { LoginUseCase } from "@/application/use-cases/auth/login.use-case";
import { GetStudentUseCase } from "@/application/use-cases/student/get-student.use-case";
import { GetStudentProfileUseCase } from "@/application/use-cases/student-profile/get-student-profile.use-case";
import { GetStudentProfilesUseCase } from "@/application/use-cases/student-profile/get-student-profiles.use-case";
import { UpdateStudentProfileUseCase } from "@/application/use-cases/student-profile/update-student-profile.use-case";
import { GetLibraryBooksUseCase } from "@/application/use-cases/library-book/get-library-books.use-case";
import { UpdateLibraryBookUseCase } from "@/application/use-cases/library-book/update-library-book.use-case";
import { BorrowBookUseCase } from "@/application/use-cases/library-book/borrow-book.use-case";
import { ReturnBookUseCase } from "@/application/use-cases/library-book/return-book.use-case";
import { GetBorrowHistoryUseCase } from "@/application/use-cases/library-book/get-borrow-history.use-case";
import { GetDocumentRequestsUseCase } from "@/application/use-cases/document-request/get-document-requests.use-case";
import { GetDocumentRequestUseCase } from "@/application/use-cases/document-request/get-document-request.use-case";
import { CreateDocumentRequestUseCase } from "@/application/use-cases/document-request/create-document-request.use-case";
import { GetComplaintsUseCase } from "@/application/use-cases/complaint/get-complaints.use-case";
import { CreateComplaintUseCase } from "@/application/use-cases/complaint/create-complaint.use-case";
import { GetStudentBalanceUseCase } from "@/application/use-cases/finance/get-balance.use-case";
import { ProcessTransactionUseCase } from "@/application/use-cases/finance/process-transaction.use-case";
import { GetAssessmentUseCase } from "@/application/use-cases/finance/get-assessment.use-case";
import { GetPaymentSlipUseCase } from "@/application/use-cases/finance/get-payment-slip.use-case";
import { GetTransactionHistoryUseCase } from "@/application/use-cases/finance/get-transaction-history.use-case";
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
import { GetStudyLoadPdfUseCase } from "@/application/use-cases/enrollment/get-study-load-pdf.use-case";
import { GetStudyLoadUseCase } from "@/application/use-cases/enrollment/get-study-load.use-case";
import { GetStudentSubjectsUseCase } from "@/application/use-cases/registration/get-student-subjects.use-case";
import { CreateSubjectUseCase } from "@/application/use-cases/subject/create-subject.use-case";
import { SubmitEvaluationUseCase } from "@/application/use-cases/evaluation/submit-evaluation.use-case";
import { GetStudentEvaluationsUseCase } from "@/application/use-cases/evaluation/get-student-evaluations.use-case";

import { db } from "@/infrastructure/db/client";
import { BorrowRecordPgRepository } from "@/infrastructure/db/repositories/borrow-record.pg.repository";
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
import { EvaluationPgRepository } from "@/infrastructure/db/repositories/evaluation.pg.repository";

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
import { EvaluationController } from "@/presentation/controllers/evaluation.controller";

// --- Repositories ---
const studentRepo = new StudentPgRepository(db);
const studentProfileRepo = new StudentProfilePgRepository(db);
const libraryBookRepo = new LibraryBookPgRepository(db);
const borrowRecordRepo = new BorrowRecordPgRepository(db);
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
const evaluationRepo = new EvaluationPgRepository(db);

// --- Use Cases ---
const getStudentUseCase = new GetStudentUseCase(studentRepo);
const loginUseCase = new LoginUseCase(studentRepo);
const getStudentProfileUseCase = new GetStudentProfileUseCase(studentProfileRepo);
const getStudentProfilesUseCase = new GetStudentProfilesUseCase(studentProfileRepo);
const updateStudentProfileUseCase = new UpdateStudentProfileUseCase(studentProfileRepo);

const getLibraryBooksUseCase = new GetLibraryBooksUseCase(libraryBookRepo);
const updateLibraryBookUseCase = new UpdateLibraryBookUseCase(libraryBookRepo);
const borrowBookUseCase = new BorrowBookUseCase(libraryBookRepo, borrowRecordRepo);
const returnBookUseCase = new ReturnBookUseCase(libraryBookRepo, borrowRecordRepo);
const getBorrowHistoryUseCase = new GetBorrowHistoryUseCase(borrowRecordRepo);

const getDocumentRequestsUseCase = new GetDocumentRequestsUseCase(documentRequestRepo, studentRepo);
const getDocumentRequestUseCase = new GetDocumentRequestUseCase(documentRequestRepo);
const createDocumentRequestUseCase = new CreateDocumentRequestUseCase(documentRequestRepo, transactionRepo);

const getComplaintsUseCase = new GetComplaintsUseCase(complaintRepo, studentRepo);
const createComplaintUseCase = new CreateComplaintUseCase(complaintRepo);

const getStudentBalanceUseCase = new GetStudentBalanceUseCase(transactionRepo, studentRepo);
const processTransactionUseCase = new ProcessTransactionUseCase(transactionRepo);
const getAssessmentUseCase = new GetAssessmentUseCase(studentRepo, studentProfileRepo, enrollmentRepo, courseRepo, transactionRepo);
const getPaymentSlipUseCase = new GetPaymentSlipUseCase(getAssessmentUseCase, transactionRepo, studentRepo);
const getTransactionHistoryUseCase = new GetTransactionHistoryUseCase(transactionRepo, studentRepo);

const getProgramsUseCase = new GetProgramsUseCase(programRepo);
const getProgramUseCase = new GetProgramUseCase(programRepo);

const getCoursesUseCase = new GetCoursesUseCase(courseRepo);

const getScheduleEntriesUseCase = new GetScheduleEntriesUseCase(scheduleEntryRepo, studentRepo);

const getGradeRecordsUseCase = new GetGradeRecordsUseCase(gradeRecordRepo, studentRepo);

const getTransactionsUseCase = new GetTransactionsUseCase(transactionRepo, studentRepo);

const getEnrollmentsUseCase = new GetEnrollmentsUseCase(enrollmentRepo, studentRepo);
const createEnrollmentUseCase = new CreateEnrollmentUseCase(enrollmentRepo, courseRepo, transactionRepo, studentRepo);
const updateEnrollmentUseCase = new UpdateEnrollmentUseCase(enrollmentRepo, courseRepo);
const deleteEnrollmentUseCase = new DeleteEnrollmentUseCase(enrollmentRepo);
const getStudyLoadPdfUseCase = new GetStudyLoadPdfUseCase(enrollmentRepo, studentProfileRepo, courseRepo);
const getStudyLoadUseCase = new GetStudyLoadUseCase(enrollmentRepo, courseRepo, studentRepo);

const getStudentSubjectsUseCase = new GetStudentSubjectsUseCase(registrationRepo);
const createSubjectUseCase = new CreateSubjectUseCase(subjectRepo);

const submitEvaluationUseCase = new SubmitEvaluationUseCase(evaluationRepo);
const getStudentEvaluationsUseCase = new GetStudentEvaluationsUseCase(evaluationRepo);

// --- Controllers ---
export const authController = new AuthController(loginUseCase, getStudentUseCase, getStudentProfileUseCase);

export const studentProfileController = new StudentProfileController(
  getStudentProfileUseCase,
  getStudentProfilesUseCase,
  updateStudentProfileUseCase,
);

export const libraryBookController = new LibraryBookController(
  getLibraryBooksUseCase,
  updateLibraryBookUseCase,
  borrowBookUseCase,
  returnBookUseCase,
  getBorrowHistoryUseCase,
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
  getAssessmentUseCase,
  getPaymentSlipUseCase,
  getTransactionHistoryUseCase,
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
  deleteEnrollmentUseCase,
  getStudyLoadPdfUseCase,
  getStudyLoadUseCase
);

export const subjectRegistrationController = new SubjectRegistrationController(
  getStudentSubjectsUseCase
);

export const subjectController = new SubjectController(createSubjectUseCase);

export const evaluationController = new EvaluationController(
  submitEvaluationUseCase,
  getStudentEvaluationsUseCase
);
