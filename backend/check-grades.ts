import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { gradeRecords } from "./src/infrastructure/db/schema/grade-record.schema.js";
import { eq } from "drizzle-orm";

async function check() {
  const studentId = "user_123";
  const records = await db.select().from(gradeRecords).where(eq(gradeRecords.studentId, studentId));
  console.log(`Found ${records.length} records for student ${studentId}`);
  records.forEach(r => {
    console.log(`- ${r.title}: ${r.gradePoint} (${r.status}) [Credits: ${r.codeCredits}, Semester: ${r.semesterLabel}]`);
  });
  process.exit(0);
}

check().catch(err => {
  console.error(err);
  process.exit(1);
});
