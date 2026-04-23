import { LoginUseCase } from "@/application/use-cases/auth/login.use-case";
import { GetStudentProfileUseCase } from "@/application/use-cases/student-profile/get-student-profile.use-case";
import { UpdateStudentProfileUseCase } from "@/application/use-cases/student-profile/update-student-profile.use-case";
import { BorrowBookUseCase } from "@/application/use-cases/book/borrow-book.use-case";
import { ReturnBookUseCase } from "@/application/use-cases/book/return-book.use-case";
import { CreateDocumentRequestUseCase } from "@/application/use-cases/document/create-request.use-case";
import { GetStudentBalanceUseCase } from "@/application/use-cases/finance/get-balance.use-case";
import { ProcessTransactionUseCase } from "@/application/use-cases/finance/process-transaction.use-case";
import { GetProgramsUseCase, ViewProgramDetailsUseCase, GetProspectusLinkUseCase } from "@/application/use-cases/program/program.use-cases";

import { db } from "@/infrastructure/db/client";
import { StudentProfilePgRepository } from "@/infrastructure/db/repositories/student-profile.pg.repository";
import { StudentPgRepository } from "@/infrastructure/db/repositories/student.pg.repository";
import { BookPgRepository } from "@/infrastructure/db/repositories/book.pg.repository";
import { BorrowRecordPgRepository } from "@/infrastructure/db/repositories/borrow-record.pg.repository";
import { DocumentRequestPgRepository } from "@/infrastructure/db/repositories/document-request.pg.repository";
import { TransactionPgRepository } from "@/infrastructure/db/repositories/transaction.pg.repository";
import { ProgramPgRepository } from "@/infrastructure/db/repositories/program.pg.repository";

import { AuthController } from "@/presentation/controllers/auth.controller";
import { StudentProfileController } from "@/presentation/controllers/student-profile.controller";
import { BookController } from "@/presentation/controllers/book.controller";
import { DocumentController } from "@/presentation/controllers/document.controller";
import { FinanceController } from "@/presentation/controllers/finance.controller";
import { ProgramController } from "@/presentation/controllers/program.controller";

// --- Repositories ---
const studentRepo = new StudentPgRepository(db);
const studentProfileRepo = new StudentProfilePgRepository(db);
const bookRepo = new BookPgRepository(db);
const borrowRecordRepo = new BorrowRecordPgRepository(db);
const documentRepo = new DocumentRequestPgRepository(db);
const transactionRepo = new TransactionPgRepository(db);
const programRepo = new ProgramPgRepository(db);

// --- Use Cases ---
const loginUseCase = new LoginUseCase(studentRepo);
const getStudentProfileUseCase = new GetStudentProfileUseCase(studentProfileRepo);
const updateStudentProfileUseCase = new UpdateStudentProfileUseCase(studentProfileRepo);

const borrowBookUseCase = new BorrowBookUseCase(bookRepo, borrowRecordRepo);
const returnBookUseCase = new ReturnBookUseCase(bookRepo, borrowRecordRepo);

const createDocumentRequestUseCase = new CreateDocumentRequestUseCase(documentRepo);

const getStudentBalanceUseCase = new GetStudentBalanceUseCase(transactionRepo);
const processTransactionUseCase = new ProcessTransactionUseCase(transactionRepo);

const getProgramsUseCase = new GetProgramsUseCase(programRepo);
const viewProgramUseCase = new ViewProgramDetailsUseCase(programRepo);
const getProspectusUseCase = new GetProspectusLinkUseCase(programRepo);

// --- Controllers ---
export const authController = new AuthController(loginUseCase);

export const studentProfileController = new StudentProfileController(
  getStudentProfileUseCase,
  updateStudentProfileUseCase,
);

export const bookController = new BookController(
  borrowBookUseCase,
  returnBookUseCase,
);

export const documentController = new DocumentController(createDocumentRequestUseCase);

export const financeController = new FinanceController(
  getStudentBalanceUseCase,
  processTransactionUseCase,
  transactionRepo
);

export const programController = new ProgramController(
  getProgramsUseCase,
  viewProgramUseCase,
  getProspectusUseCase
);
