export type TransactionType = 'CHARGE' | 'PAYMENT';
export type PaymentMethod = 'CASH' | 'GCASH' | 'CREDIT_CARD' | 'BANK_TRANSFER' | 'NONE';
export type TransactionStatus = 'PENDING' | 'COMPLETED' | 'FAILED';

export type Transaction = {
  id: string;
  studentId: string;
  type: TransactionType;
  amount: number; // We will store this as a whole number (e.g., 1500 for ₱1500)
  method: PaymentMethod;
  status: TransactionStatus;
  referenceId: string; // The receipt or assessment number (e.g., TXN-8892)
  description: string; // e.g., "Tuition Fee" or "Online Payment"
  createdAt: Date;
};