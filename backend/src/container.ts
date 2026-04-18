import { CreateStudentUseCase } from "@/application/use-cases/student/create-student.use-case";
import { DeleteStudentUseCase } from "@/application/use-cases/student/delete-student.use-case";
import { GetStudentUseCase } from "@/application/use-cases/student/get-student.use-case";
import { UpdateStudentUseCase } from "@/application/use-cases/student/update-student.use-case";
import { db } from "@/infrastructure/db/client";
import { StudentPgRepository } from "@/infrastructure/db/repositories/student.pg.repository";
import { StudentController } from "@/presentation/controllers/student.controller";

const studentRepo = new StudentPgRepository(db);

const createStudentUseCase = new CreateStudentUseCase(studentRepo);
const getStudentUseCase = new GetStudentUseCase(studentRepo);
const updateStudentUseCase = new UpdateStudentUseCase(studentRepo);
const deleteStudentUseCase = new DeleteStudentUseCase(studentRepo);

export const studentController = new StudentController(
  createStudentUseCase,
  getStudentUseCase,
  updateStudentUseCase,
  deleteStudentUseCase,
);
