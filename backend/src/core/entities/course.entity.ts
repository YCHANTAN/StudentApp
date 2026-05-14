export type CourseStatus = 'Enrolled' | 'Completed' | 'Waitlisted';

export type Course = {
  id: string;
  code: string;
  title: string;
  semesterTitle?: string;
  instructor?: string;
  units?: number;
  schedule?: string;
  location?: string;
  grade?: string;
  waitlistStatus?: string;
  status?: CourseStatus;
  tuition?: number;
  remainingSlots?: number;
  isLocked?: boolean;
  lockReason?: string;
  programId?: string;
  createdAt: Date;
};
