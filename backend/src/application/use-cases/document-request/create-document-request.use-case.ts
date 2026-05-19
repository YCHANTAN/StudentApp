import type { DocumentRequestRepository } from '@/application/repositories/document-request.repository';
import type { TransactionRepository } from '@/application/repositories/transaction.repository';
import type { StudentRepository } from '@/application/repositories/student.repository';
import type { CreateDocumentRequestInput } from '@/application/dtos/document-request.dto';
import type { DocumentRequest } from '@/core/entities/document-request.entity';
import { isUuid } from '@/presentation/lib/uuid.helper';

export class CreateDocumentRequestUseCase {
  constructor(
    private readonly documentRequestRepo: DocumentRequestRepository,
    private readonly transactionRepo: TransactionRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(input: CreateDocumentRequestInput): Promise<DocumentRequest> {
    let targetId = input.studentId;

    // Resolve STU-ID to database UUID if necessary
    if (!isUuid(input.studentId)) {
      const student = await this.studentRepo.findByStudentId(input.studentId);
      if (student) {
        targetId = student.id;
      }
    }

    const reference = `REF-${Math.random().toString(36).substring(2, 9).toUpperCase()}`;
    
    const request: DocumentRequest = {
      id: crypto.randomUUID(),
      studentId: targetId,
      type: input.type,
      purpose: input.purpose,
      reference,
      status: 'PROCESSING',
      submittedAt: new Date(),
      updatedAt: new Date(),
    };

    if (input.program !== undefined) request.program = input.program;
    if (input.yearLevel !== undefined) request.yearLevel = input.yearLevel;
    if (input.copies !== undefined) request.copies = input.copies;
    if (input.deliveryMethod !== undefined) request.deliveryMethod = input.deliveryMethod;

    // Determine Fee based on type
    let feeAmount = "150.00";
    if (input.type === 'TOR') {
        const copies = input.copies || 1;
        feeAmount = (150 * copies + 30).toFixed(2);
    } else if (input.type === 'GoodMoral' || input.type === 'COE') {
        feeAmount = "100.00";
    }

    // Create Finance Transaction (FEE)
    await this.transactionRepo.save({
        id: crypto.randomUUID(),
        studentId: targetId,
        title: `Fee for ${input.type} Request`,
        type: 'FEE',
        amount: feeAmount,
        method: 'SYSTEM',
        status: 'PENDING',
        referenceId: reference,
        description: `Charges for ${input.type} document request (${reference})`,
        date: new Date(),
        isPaid: false,
        createdAt: new Date()
    });

    return this.documentRequestRepo.save(request);
  }
}
