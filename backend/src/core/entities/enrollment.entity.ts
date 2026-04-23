// src/core/entities/enrollment.entity.ts

export type EnrollmentStatus = 'Draft' | 'Confirmed' | 'Adjusted';

export type Enrollment = {
  id: string;
  studentId: string;
  courseIds: string[];
  fullName: string;
  studentIdNumber: string;
  emailAddress: string;
  phoneNumber: string;
  emergencyContactName: string;
  relationship: string;
  emergencyPhone: string;
  selectedCredits: number;
  estimatedTuition: number;
  status: EnrollmentStatus;
  paymentMethod: string;
  isPaid: boolean;
  createdAt: Date;
  updatedAt: Date;
};
