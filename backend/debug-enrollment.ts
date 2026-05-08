import { db } from "./src/infrastructure/db/client.js";
import { enrollments, enrollmentCourses } from "./src/infrastructure/db/schema/enrollment.schema.js";
import { courses } from "./src/infrastructure/db/schema/course.schema.js";
import { eq } from "drizzle-orm";

async function listDetailedEnrollments() {
  const studentId = "user_123";
  const result = await db.select().from(enrollments).where(eq(enrollments.studentId, studentId));
  
  for (const enr of result) {
    const courseDetails = await db
      .select({ code: courses.code, title: courses.title })
      .from(enrollmentCourses)
      .innerJoin(courses, eq(enrollmentCourses.courseId, courses.id))
      .where(eq(enrollmentCourses.enrollmentId, enr.id));
    
    console.log(`Enrollment ID: ${enr.id}`);
    console.log(`Status: ${enr.status}, Date: ${enr.createdAt}`);
    console.log(`Courses: ${courseDetails.map(c => c.code).join(", ")}`);
    console.log("-------------------");
  }
  process.exit(0);
}

listDetailedEnrollments().catch(console.error);
