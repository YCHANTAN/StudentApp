import { db } from "./src/infrastructure/db/client.js";
import { enrollments, enrollmentCourses } from "./src/infrastructure/db/schema/enrollment.schema.js";
import { eq, and, ne } from "drizzle-orm";

async function cleanupEnrollments() {
  const studentId = "user_123";
  
  console.log(`Cleaning up duplicate PENDING enrollments for ${studentId}...`);
  
  // Find all PENDING enrollments for this student
  const pending = await db
    .select({ id: enrollments.id })
    .from(enrollments)
    .where(and(
      eq(enrollments.studentId, studentId),
      eq(enrollments.status, "PENDING")
    ));

  console.log(`Found ${pending.length} PENDING enrollments.`);

  for (const enr of pending) {
    console.log(`Deleting PENDING enrollment ${enr.id}...`);
    await db.delete(enrollmentCourses).where(eq(enrollmentCourses.enrollmentId, enr.id));
    await db.delete(enrollments).where(eq(enrollments.id, enr.id));
  }

  console.log("Cleanup complete.");
  process.exit(0);
}

cleanupEnrollments().catch(console.error);
