export type DocumentType = 'TOR' | 'GoodMoral' | 'COE';
export type DeliveryMethod = 'Pickup' | 'Courier';
export type DocumentRequestStatus = 'PROCESSING' | 'ACCEPTED' | 'READY_FOR_PICKUP';

export type DocumentRequest = {
  id: string;
  studentId: string;
  type: DocumentType;
  purpose: string;
  program?: string;
  yearLevel?: string;
  copies?: number;
  deliveryMethod?: DeliveryMethod;
  reference: string;
  status: DocumentRequestStatus;
  submittedAt: Date;
  updatedAt: Date;
};
