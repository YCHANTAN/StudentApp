export type TransactionType = 'PAYMENT' | 'FEE' | 'REFUND';
export type TransactionStatus = 'PENDING' | 'COMPLETED' | 'FAILED';

export type Transaction = {
  id: string;
  studentId: string;
  title: string;
  type: TransactionType;
  amount: string;
  method: string;
  status: TransactionStatus;
  referenceId: string;
  description: string | null;
  date: Date;
  isPaid: boolean;
  createdAt: Date;
};
