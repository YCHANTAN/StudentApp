import "dotenv/config";
import * as bcrypt from "bcryptjs";
import { db } from "./src/infrastructure/db/client.js";
import { students } from "./src/infrastructure/db/schema/student.schema.js";
import { studentProfiles } from "./src/infrastructure/db/schema/student-profile.schema.js";
import { courses } from "./src/infrastructure/db/schema/course.schema.js";
import { gradeRecords } from "./src/infrastructure/db/schema/grade-record.schema.js";
import { transactions } from "./src/infrastructure/db/schema/transaction.schema.js";
import { documentRequests } from "./src/infrastructure/db/schema/document-request.schema.js";
import { complaints } from "./src/infrastructure/db/schema/complaint.schema.js";
import { programs as programsTable } from "./src/infrastructure/db/schema/program.schema.js";
import { scheduleEntries } from "./src/infrastructure/db/schema/schedule-entry.schema.js";
import { evaluations } from "./src/infrastructure/db/schema/evaluation.schema.js";
import { enrollments, enrollmentCourses } from "./src/infrastructure/db/schema/enrollment.schema.js";

async function seed() {
  const passwordHash = await bcrypt.hash("password123", 10);
  const studentId = "user_123";
  
  console.log("Seeding student...");
  // 1. Seed the Student Auth record
  await db.insert(students).values({
    id: studentId,
    studentId: "STU-2024-1",
    fullName: "Christian Osorno",
    email: "christian@example.com",
    passwordHash: passwordHash
  }).onConflictDoUpdate({
    target: students.id,
    set: { 
      studentId: "STU-2024-1",
      passwordHash, 
      fullName: "Christian Osorno",
      email: "christian@example.com"
    }
  });

  // 2. Seed the Student Profile record
  await db.insert(studentProfiles).values({
    id: studentId,
    fullName: "Christian Osorno",
    emailAddress: "christian@example.com",
    phoneNumber: "0917-555-0123",
    accountLabel: "Computer Science Student",
    programSummary: "BS Computer Science • Year 2",
    twoFactorStatus: "Disabled",
    emergencyContactName: "Maria Osorno",
    emergencyContactRelationship: "Mother",
    emergencyContactPhoneNumber: "0917-555-0987",
    emailNotifications: true,
    smsNotifications: false,
    systemAlerts: true
  }).onConflictDoUpdate({
    target: studentProfiles.id,
    set: { 
        fullName: "Christian Osorno",
        programSummary: "BS Computer Science • Year 2",
        accountLabel: "Computer Science Student"
    }
  });

  console.log("Seeding schedule entries...");
  // Seed Schedule Entries
  const scheduleEntryList = [
    {
        studentId,
        dayLabel: "Monday",
        courseCode: "CS301",
        courseTitle: "Advanced Algorithms",
        timeRange: "10:00 AM — 11:30 AM",
        room: "Engineering Hall, Rm 402",
        instructor: "Dr. Helena Vance"
    },
    {
        studentId,
        dayLabel: "Tuesday",
        courseCode: "MATH402",
        courseTitle: "Stochastic Processes",
        timeRange: "01:00 PM — 02:30 PM",
        room: "Science Building, Rm 105",
        instructor: "Prof. Julian Thorne"
    },
    {
        studentId,
        dayLabel: "Wednesday",
        courseCode: "CS301",
        courseTitle: "Advanced Algorithms",
        timeRange: "10:00 AM — 11:30 AM",
        room: "Engineering Hall, Rm 402",
        instructor: "Dr. Helena Vance"
    }
  ];

  for (const entry of scheduleEntryList) {
    await db.insert(scheduleEntries).values(entry).onConflictDoNothing();
  }

  console.log("Seeding courses...");
  // 3. Seed Courses
  const courseList = [
    {
      id: "550e8400-e29b-41d4-a716-446655440003",
      code: "CS301",
      title: "Advanced Algorithms",
      semesterTitle: "Spring Semester 2024",
      instructor: "Dr. Helena Vance",
      units: 4,
      schedule: "Mon/Wed 10:00 AM — 11:30 AM",
      location: "Engineering Hall, Rm 402",
      progress: "0.65",
      status: "Enrolled",
      remainingSlots: 15,
      tuition: "710.00"
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440004",
      code: "MATH402",
      title: "Stochastic Processes",
      semesterTitle: "Spring Semester 2024",
      instructor: "Prof. Julian Thorne",
      units: 3,
      schedule: "Tue/Thu 01:00 PM — 02:30 PM",
      location: "Science Building, Rm 105",
      progress: "0.40",
      status: "Enrolled",
      remainingSlots: 8,
      tuition: "530.00"
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440005",
      code: "CS320",
      title: "Machine Learning Basics",
      semesterTitle: "Spring Semester 2024",
      instructor: "Dr. Sarah Jenkins",
      units: 4,
      schedule: "Friday 09:00 AM — 12:00 PM",
      location: "Online Sync",
      progress: "0.85",
      status: "Enrolled",
      remainingSlots: 0,
      tuition: "720.00"
    },
    {
        id: "550e8400-e29b-41d4-a716-446655440014",
        code: "CS305",
        title: "Database Management",
        semesterTitle: "Spring Semester 2024",
        instructor: "Prof. Michael Chen",
        units: 3,
        schedule: "Tue/Thu 2:00 PM — 3:30 PM",
        location: "Engineering Hall, Rm 301",
        remainingSlots: 20,
        tuition: "530.00",
        status: "Available"
    },
    {
        id: "550e8400-e29b-41d4-a716-446655440015",
        code: "UI102",
        title: "User Interface Design",
        semesterTitle: "Spring Semester 2024",
        instructor: "Amanda Waller",
        units: 3,
        schedule: "Friday 09:00 AM — 12:00 PM",
        location: "Design Studio A",
        remainingSlots: 12,
        tuition: "530.00",
        status: "Available"
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440006",
      code: "CS201",
      title: "Data Structures",
      semesterTitle: "2nd Semester 3rd Year",
      instructor: "Dr. Alan Turing",
      units: 3,
      grade: "1.25",
      progress: "1.00",
      status: "Completed",
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440007",
      code: "MATH302",
      title: "Linear Algebra",
      semesterTitle: "2nd Semester 3rd Year",
      instructor: "Prof. Katherine Johnson",
      units: 3,
      grade: "1.50",
      progress: "1.00",
      status: "Completed",
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440008",
      code: "CS205",
      title: "Operating Systems",
      semesterTitle: "2nd Semester 3rd Year",
      instructor: "Dr. Grace Hopper",
      units: 3,
      grade: "1.00",
      progress: "1.00",
      status: "Completed",
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440009",
      code: "CS401",
      title: "Artificial Intelligence",
      semesterTitle: "2nd Semester 3rd Year",
      instructor: "Prof. Robert Smith",
      units: 4,
      schedule: "Mon/Wed 2:00 PM — 3:30 PM",
      waitlistStatus: "Waitlisted #15",
      progress: "0.30",
      status: "Waitlisted",
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440010",
      code: "MATH501",
      title: "Advanced Calculus",
      semesterTitle: "2nd Semester 3rd Year",
      instructor: "Dr. Elena Gilbert",
      units: 3,
      schedule: "Tue/Thu 9:00 AM — 10:30 AM",
      waitlistStatus: "Waitlisted #3",
      progress: "0.85",
      status: "Waitlisted",
    }
  ] as any[];

  for (const course of courseList) {
    await db.insert(courses).values(course).onConflictDoUpdate({
      target: courses.id,
      set: course
    });
  }

  console.log("Seeding grade records...");
  // 4. Seed Grade Records
  const gradeRecordList = [
    // 2nd Semester 3rd Year
    {
        studentId,
        title: "Data Structures",
        codeCredits: "CS201 • 3 Credits",
        gradePoint: "1.25",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 3rd Year",
        remarks: "Excellent"
    },
    {
        studentId,
        title: "Linear Algebra",
        codeCredits: "MATH302 • 3 Credits",
        gradePoint: "1.50",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 3rd Year",
        remarks: "Passed"
    },
    {
        studentId,
        title: "Operating Systems",
        codeCredits: "CS205 • 3 Credits",
        gradePoint: "1.00",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 3rd Year",
        remarks: "Outstanding"
    },
    {
        studentId,
        title: "Computer Networks",
        codeCredits: "CS306 • 3 Credits",
        gradePoint: "1.75",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 3rd Year",
        remarks: "Good"
    },
    // 1st Semester 3rd Year
    {
        studentId,
        title: "Discrete Mathematics",
        codeCredits: "MATH101 • 3 Credits",
        gradePoint: "1.75",
        status: "COMPLETED",
        semesterLabel: "1st Semester 3rd Year",
        remarks: "Good"
    },
    {
        studentId,
        title: "Object-Oriented Programming",
        codeCredits: "CS202 • 3 Credits",
        gradePoint: "1.25",
        status: "COMPLETED",
        semesterLabel: "1st Semester 3rd Year",
        remarks: "Very Good"
    },
    {
        studentId,
        title: "Software Engineering 1",
        codeCredits: "CS303 • 3 Credits",
        gradePoint: "1.50",
        status: "COMPLETED",
        semesterLabel: "1st Semester 3rd Year",
        remarks: "Passed"
    },
    // 2nd Semester 2nd Year
    {
        studentId,
        title: "Intro to Programming",
        codeCredits: "CS101 • 3 Credits",
        gradePoint: "1.25",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 2nd Year",
        remarks: "Very Good"
    },
    {
        studentId,
        title: "Digital Logic Design",
        codeCredits: "CS105 • 3 Credits",
        gradePoint: "1.50",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 2nd Year",
        remarks: "Passed"
    },
    {
        studentId,
        title: "Ethics in Computing",
        codeCredits: "GE104 • 3 Credits",
        gradePoint: "1.25",
        status: "COMPLETED",
        semesterLabel: "2nd Semester 2nd Year",
        remarks: "Very Good"
    },
    // 1st Semester 2nd Year
    {
        studentId,
        title: "College Algebra",
        codeCredits: "MATH100 • 3 Credits",
        gradePoint: "2.00",
        status: "COMPLETED",
        semesterLabel: "1st Semester 2nd Year",
        remarks: "Satisfactory"
    },
    {
        studentId,
        title: "Art Appreciation",
        codeCredits: "GE101 • 3 Credits",
        gradePoint: "1.00",
        status: "COMPLETED",
        semesterLabel: "1st Semester 2nd Year",
        remarks: "Outstanding"
    }
  ] as any[];

  for (const grade of gradeRecordList) {
    await db.insert(gradeRecords).values(grade).onConflictDoNothing();
  }

  console.log("Seeding transactions...");
  // 5. Seed Transactions
  const transactionList = [
    {
      id: "550e8400-e29b-41d4-a716-446655440001",
      studentId,
      title: "Enrollment Fee",
      type: "FEE",
      amount: "1500.00",
      method: "SYSTEM",
      status: "COMPLETED",
      referenceId: "REF-001",
      description: "Initial enrollment fee",
      isPaid: true,
      date: new Date()
    },
    {
      id: "550e8400-e29b-41d4-a716-446655440002",
      studentId,
      title: "Tuition Downpayment",
      type: "PAYMENT",
      amount: "500.00",
      method: "GCash",
      status: "COMPLETED",
      referenceId: "REF-002",
      description: "Partial tuition payment",
      isPaid: true,
      date: new Date()
    },
    {
        id: "550e8400-e29b-41d4-a716-446655440011",
        studentId,
        title: "Library Fine",
        type: "FEE",
        amount: "50.00",
        method: "SYSTEM",
        status: "PENDING",
        referenceId: "REF-003",
        description: "Late book return",
        isPaid: false,
        date: new Date()
    }
  ] as any[];

  for (const txn of transactionList) {
    await db.insert(transactions).values(txn).onConflictDoUpdate({
      target: transactions.id,
      set: txn
    });
  }

  console.log("Seeding document requests...");
  // 6. Seed Document Requests (Services)
  const docRequests = [
    {
        id: "550e8400-e29b-41d4-a716-446655440012",
        studentId,
        type: "TOR",
        purpose: "Job Application",
        program: "BS Computer Science",
        yearLevel: "Year 3",
        copies: 1,
        deliveryMethod: "Pickup",
        reference: "REQ-TOR-001",
        status: "READY_FOR_PICKUP",
        submittedAt: new Date()
    },
    {
        id: "550e8400-e29b-41d4-a716-446655440013",
        studentId,
        type: "GoodMoral",
        purpose: "Scholarship",
        program: "BS Computer Science",
        yearLevel: "Year 3",
        copies: 1,
        deliveryMethod: "Pickup",
        reference: "REQ-GM-001",
        status: "PROCESSING",
        submittedAt: new Date()
    }
  ] as any[];

  for (const doc of docRequests) {
    await db.insert(documentRequests).values(doc).onConflictDoUpdate({
        target: documentRequests.id,
        set: doc
    });
  }
    
  console.log("Seeding complaints...");
  // 7. Seed Complaints
  const complaintList = [
    {
      studentId,
      title: "Classroom AC issue in Rm 402",
      status: "IN_REVIEW"
    },
    {
      studentId,
      title: "Inquiry about scholarship extension",
      status: "RESOLVED"
    }
  ] as any[];

  for (const comp of complaintList) {
    await db.insert(complaints).values(comp).onConflictDoNothing();
  }

  console.log("Seeding programs...");
  // 8. Seed Programs
  const programList = [
    {
      id: "PROG-CS",
      title: "BS Computer Science",
      badgeText: "Enrollment Open",
      badgeVariant: "Primary",
      scheduleLine: "4 Years • Full Time",
      description: "Master software engineering, AI, and cybersecurity with our industry-leading curriculum and hands-on laboratory modules.",
      category: "Undergraduate",
      prospectusUrl: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
    },
    {
      id: "PROG-BBA",
      title: "BBA Business Admin",
      badgeText: "Next Intake: Sept 2024",
      badgeVariant: "Accent",
      scheduleLine: "4 Years • Global Track",
      description: "Equip yourself with strategic leadership skills, financial acumen, and entrepreneurial mindset for the modern corporate world.",
      category: "Undergraduate",
      prospectusUrl: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
    },
    {
      id: "PROG-MSDA",
      title: "MS Data Analytics",
      badgeText: "Enrollment Open",
      badgeVariant: "Primary",
      scheduleLine: "2 Years • Postgraduate",
      description: "Advanced statistical modeling and machine learning applications for driving data-informed business decisions.",
      category: "Postgraduate",
      prospectusUrl: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
    },
    {
      id: "PROG-ARCH",
      title: "BS Architecture",
      badgeText: "Limited Seats",
      badgeVariant: "Accent",
      scheduleLine: "5 Years • Studio Based",
      description: "Explore sustainable design, urban planning, and historic conservation through creative architectural practice.",
      category: "Undergraduate",
      prospectusUrl: "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
    }
  ] as any[];

  for (const prog of programList) {
  await db.insert(programsTable).values(prog).onConflictDoUpdate({
    target: programsTable.id,
    set: prog
  });
  }

  console.log("Seeding evaluations...");
  const evaluationList = [
  {
    studentId,
    courseId: "550e8400-e29b-41d4-a716-446655440003", // CS301
    teachingQuality: 5,
    courseMaterials: 4,
    punctuality: 5,
    comments: "Great course! Very challenging but rewarding."
  }
  ];

  for (const evaluation of evaluationList) {
    await db.insert(evaluations).values(evaluation).onConflictDoNothing();
  }

  console.log("Seeding enrollments...");
  const enrollmentId = "550e8400-e29b-41d4-a716-446655440100";
  await db.insert(enrollments).values({
    id: enrollmentId,
    studentId,
    status: "APPROVED",
    totalUnits: 11,
    totalTuition: "1960.00",
  }).onConflictDoUpdate({
    target: enrollments.id,
    set: { status: "APPROVED", totalUnits: 11, totalTuition: "1960.00" }
  });

  const enrollmentCourseList = [
    { enrollmentId, courseId: "550e8400-e29b-41d4-a716-446655440003" }, // CS301
    { enrollmentId, courseId: "550e8400-e29b-41d4-a716-446655440004" }, // MATH402
    { enrollmentId, courseId: "550e8400-e29b-41d4-a716-446655440005" }, // CS320
  ];

  for (const ec of enrollmentCourseList) {
    await db.insert(enrollmentCourses).values(ec).onConflictDoNothing();
  }

  console.log("Seeded successfully.");

  process.exit(0);
}

seed().catch(err => {
  console.error(err);
  process.exit(1);
});

