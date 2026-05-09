import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { gradeRecords } from "./src/infrastructure/db/schema/grade-record.schema.js";
import { eq } from "drizzle-orm";
import { GetStudyLoadUseCase } from "./src/application/use-cases/enrollment/get-study-load.use-case.js";
import { EnrollmentPgRepository } from "./src/infrastructure/db/repositories/enrollment.pg.repository.js";
import { CoursePgRepository } from "./src/infrastructure/db/repositories/course.pg.repository.js";
import { StudentPgRepository } from "./src/infrastructure/db/repositories/student.pg.repository.js";

async function debugData() {
  const studentId = "user_123";
  
  console.log("--- Grade Records ---");
  const grades = await db.select().from(gradeRecords).where(eq(gradeRecords.studentId, studentId));
  console.log(grades.map(g => ({ title: g.title, code: g.codeCredits, status: g.status })));

  console.log("\n--- Study Load (Backend Logic) ---");
  const enrollmentRepo = new EnrollmentPgRepository(db as any);
  const courseRepo = new CoursePgRepository(db as any);
  const studentRepo = new StudentPgRepository(db as any);
  const useCase = new GetStudyLoadUseCase(enrollmentRepo, courseRepo, studentRepo);
  
  const studyLoad = await useCase.execute(studentId);
  console.log("Courses in Study Load:", studyLoad.courses.map(c => ({ code: c.code, title: c.title })));
  
  process.exit(0);
}

debugData().catch(console.error);
