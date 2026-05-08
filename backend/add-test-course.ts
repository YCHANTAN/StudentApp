import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { courses } from "./src/infrastructure/db/schema/course.schema.js";

async function addTestCourse() {
  console.log("Adding test course...");
  
  const newCourse = {
    id: crypto.randomUUID(),
    code: "CS402",
    title: "Machine Learning",
    semesterTitle: "Spring 2024",
    instructor: "Dr. Alan Turing",
    units: 3,
    schedule: "Tue/Thu | 09:00 AM — 10:30 AM",
    location: "Lab 3, Tech Building",
    status: "Waitlisted", // Status for available courses in the list
    tuition: "4500.00",
    remainingSlots: 25,
    isLocked: false,
    createdAt: new Date(),
  };

  try {
    await db.insert(courses).values(newCourse).onConflictDoNothing();
    console.log("Successfully added CS402 - Machine Learning");
  } catch (error) {
    console.error("Error adding course:", error);
  }

  process.exit(0);
}

addTestCourse();
