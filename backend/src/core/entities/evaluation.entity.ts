export interface Evaluation {
  id: string;
  studentId: string;
  courseId: string;
  teachingQuality: number;
  courseMaterials: number;
  punctuality: number;
  comments?: string;
  createdAt: Date;
}
