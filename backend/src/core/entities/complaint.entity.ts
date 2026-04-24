export type ComplaintStatus = 'IN_REVIEW' | 'RESOLVED';

export type Complaint = {
  id: string;
  studentId: string;
  title: string;
  status: ComplaintStatus;
  createdAt: Date;
  updatedAt: Date;
};
