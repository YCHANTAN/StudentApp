import { LoginUseCase } from "@/application/use-cases/auth/login.use-case";
import { CreateStudentUseCase } from "@/application/use-cases/student/create-student.use-case";
import { DeleteStudentUseCase } from "@/application/use-cases/student/delete-student.use-case";
import { GetStudentUseCase } from "@/application/use-cases/student/get-student.use-case";
import { UpdateStudentUseCase } from "@/application/use-cases/student/update-student.use-case";
import { db } from "@/infrastructure/db/client";
import { StudentPgRepository } from "@/infrastructure/db/repositories/student.pg.repository";
import { AuthController } from "@/presentation/controllers/auth.controller";
import { StudentController } from "@/presentation/controllers/student.controller";

import { BookPgRepository } from "@/infrastructure/db/repositories/book.pg.repository";
import { BorrowRecordPgRepository } from "@/infrastructure/db/repositories/borrow-record.pg.repository";
import { BorrowBookUseCase } from "@/application/use-cases/book/borrow-book.use-case";
import { ReturnBookUseCase } from "@/application/use-cases/book/return-book.use-case";
import { BookController } from "@/presentation/controllers/book.controller";

import { DocumentRequestPgRepository } from "@/infrastructure/db/repositories/document-request.pg.repository";
import { CreateDocumentRequestUseCase } from "@/application/use-cases/document/create-request.use-case";
import { DocumentController } from "@/presentation/controllers/document.controller";

import { TransactionPgRepository } from '@/infrastructure/db/repositories/transaction.pg.repository';
import { GetStudentBalanceUseCase } from '@/application/use-cases/finance/get-balance.use-case';
import { ProcessTransactionUseCase } from '@/application/use-cases/finance/process-transaction.use-case';
import { FinanceController } from '@/presentation/controllers/finance.controller';

import { ProgramPgRepository } from '@/infrastructure/db/repositories/program.pg.repository';
import { 
  GetProgramsUseCase, 
  ViewProgramDetailsUseCase, 
  GetProspectusLinkUseCase 
} from '@/application/use-cases/program/program.use-cases';
import { ProgramController } from '@/presentation/controllers/program.controller';

import { SubjectRegistrationPgRepository } from '@/infrastructure/db/repositories/subject-registration.pg.repository';
import { GetStudentSubjectsUseCase } from '@/application/use-cases/registration/get-student-subjects.use-case';
import { SubjectRegistrationController } from '@/presentation/controllers/subject-registration.controller';

// --- Student Wiring ---
const studentRepo = new StudentPgRepository(db);

const createStudentUseCase = new CreateStudentUseCase(studentRepo);
const getStudentUseCase = new GetStudentUseCase(studentRepo);
const updateStudentUseCase = new UpdateStudentUseCase(studentRepo);
const deleteStudentUseCase = new DeleteStudentUseCase(studentRepo);
const loginUseCase = new LoginUseCase(studentRepo);

export const studentController = new StudentController(
  createStudentUseCase,
  getStudentUseCase,
  updateStudentUseCase,
  deleteStudentUseCase,
);

export const authController = new AuthController(loginUseCase);
