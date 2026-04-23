import "dotenv/config";
import * as bcrypt from "bcryptjs";
import { db } from "./src/infrastructure/db/client.js";
import { students } from "./src/infrastructure/db/schema/student.schema.js";
import { studentProfiles } from "./src/infrastructure/db/schema/student-profile.schema.js";
import { subjects, sections, subjectRegistrations } from "./src/infrastructure/db/schema/subject-registration.schema.js";

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

  // 3. Seed Subjects
  const subjectList = [
    { id: 'CS301', title: 'Data Structures and Algorithms', units: 3 },
    { id: 'CS302', title: 'Database Systems', units: 3 },
    { id: 'CS101', title: 'Introduction to Programming', units: 3 },
    { id: 'MATH201', title: 'Calculus I', units: 4 },
  ];

  for (const subject of subjectList) {
    await db.insert(subjects).values(subject).onConflictDoNothing();
  }

  // 4. Seed Sections
  const sectionList = [
    { id: 'CS301-A', subjectId: 'CS301', term: 'Spring Semester 2024', instructorName: 'Dr. Smith', timeSlots: 'MW 9:00 AM - 10:30 AM', location: 'Room 401' },
    { id: 'CS302-B', subjectId: 'CS302', term: 'Spring Semester 2024', instructorName: 'Prof. Jones', timeSlots: 'TTh 1:00 PM - 2:30 PM', location: 'Lab 2' },
    { id: 'CS101-A', subjectId: 'CS101', term: 'Fall Semester 2023', instructorName: 'Dr. White', timeSlots: 'MWF 10:00 AM - 11:00 AM', location: 'Room 101' },
    { id: 'MATH201-C', subjectId: 'MATH201', term: 'Spring Semester 2024', instructorName: 'Dr. Brown', timeSlots: 'TTh 3:00 PM - 5:00 PM', location: 'Room 302' },
  ];

  for (const section of sectionList) {
    await db.insert(sections).values(section).onConflictDoNothing();
  }

  // 5. Seed Subject Registrations
  const registrationList = [
    { studentId, sectionId: 'CS301-A', status: 'Enrolled', progressPercentage: "45.00" },
    { studentId, sectionId: 'CS302-B', status: 'Waitlisted', progressPercentage: "0.00" },
    { studentId, sectionId: 'CS101-A', status: 'Completed', progressPercentage: "100.00" },
  ] as any[];

  for (const reg of registrationList) {
    await db.insert(subjectRegistrations).values(reg).onConflictDoNothing();
  }
    
  console.log("Seeded student S1001, subjects, and registrations.");
  process.exit(0);
}

seed().catch(err => {
  console.error(err);
  process.exit(1);
});
