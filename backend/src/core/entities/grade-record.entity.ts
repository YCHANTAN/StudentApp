export type GradeRecordStatus = 'COMPLETED' | 'IN_PROGRESS';

export type GradeRecord = {
  id: string;
  studentId: string;
  title: string;
  codeCredits: string;
  gradePoint: string;
  status: GradeRecordStatus;
  semesterLabel?: string;
  createdAt: Date;
};
