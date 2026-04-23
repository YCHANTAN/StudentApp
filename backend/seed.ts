import "dotenv/config";
import * as bcrypt from "bcryptjs";
import { db } from "./src/infrastructure/db/client.js";
import { students } from "./src/infrastructure/db/schema/student.schema.js";
import { studentProfiles } from "./src/infrastructure/db/schema/student-profile.schema.js";

async function seed() {
  const passwordHash = await bcrypt.hash("password123", 10);
  const studentId = "user_123";
  
  // 1. Seed the Student Auth record
  await db.insert(students).values({
    id: studentId,
    studentId: "S1001",
    fullName: "John Doe",
    email: "john@example.com",
    passwordHash: passwordHash
  }).onConflictDoUpdate({
    target: students.studentId,
    set: { passwordHash, id: studentId }
  });

  // 2. Seed the Student Profile record
  await db.insert(studentProfiles).values({
    id: studentId,
    fullName: "John Doe",
    emailAddress: "john@example.com",
    phoneNumber: "09123456789",
    accountLabel: "Standard Student",
    programSummary: "BS Computer Science",
    twoFactorStatus: "Disabled",
    emergencyContactName: "Jane Doe",
    emergencyContactRelationship: "Mother",
    emergencyContactPhoneNumber: "09987654321",
    emailNotifications: true,
    smsNotifications: true,
    systemAlerts: true
  }).onConflictDoUpdate({
    target: studentProfiles.id,
    set: { fullName: "John Doe" }
  });
    
  console.log("Seeded student S1001 and their profile with ID: " + studentId);
  process.exit(0);
}

seed().catch(err => {
  console.error(err);
  process.exit(1);
});
