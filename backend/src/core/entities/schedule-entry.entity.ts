export type ScheduleEntry = {
  id: string;
  studentId: string;
  dayLabel: string;
  day?: string; // Alias for dayLabel for frontend compatibility
  courseCode: string;
  courseTitle: string;
  timeRange: string;
  room: string;
  instructor: string;
  createdAt: Date;
};
