export type EnrollmentStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

export type Enrollment = {
  id: string;
  studentId: string;
  status: EnrollmentStatus;
  totalUnits: number;
  totalTuition: number;
  courseIds: string[];
  createdAt: Date;
  updatedAt: Date;
};
