export type DocumentType = 'TOR' | 'GOOD_MORAL' | 'COE';
export type RequestStatus = 'PENDING' | 'PROCESSING' | 'READY' | 'COMPLETED' | 'REJECTED';

export type DocumentRequest = {
  id: string;
  studentId: string;
  documentType: DocumentType;
  purpose: string;
  copies: number;
  status: RequestStatus;
  notes: string | null;
  requestedAt: Date;
  updatedAt: Date;
};