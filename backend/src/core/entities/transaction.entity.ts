export type Transaction = {
  id: string;
  studentId: string;
  title: string;
  date: Date;
  amount: string;
  isPaid: boolean;
  createdAt: Date;
};
