import type { GetAssessmentUseCase } from "./get-assessment.use-case";
import type { TransactionRepository } from "@/application/repositories/transaction.repository";
import type { StudentRepository } from "@/application/repositories/student.repository";
import type { PaymentSlip } from "@/application/dtos/finance.dto";

export class GetPaymentSlipUseCase {
  constructor(
    private readonly getAssessmentUseCase: GetAssessmentUseCase,
    private readonly transactionRepo: TransactionRepository,
    private readonly studentRepo: StudentRepository
  ) {}

  async execute(studentId: string): Promise<PaymentSlip> {
    const assessment = await this.getAssessmentUseCase.execute(studentId);
    
    let internalId = studentId;
    const studentAuth = await this.studentRepo.findByStudentId(studentId);
    if (studentAuth) {
        internalId = studentAuth.id;
    }

    const transactions = await this.transactionRepo.findByStudentId(internalId);
    
    const totalPaid = transactions
      .filter(t => t.type === 'PAYMENT' && t.status === 'COMPLETED')
      .reduce((sum, t) => sum + parseFloat(t.amount), 0);

    const latestPayment = transactions
      .filter(t => t.type === 'PAYMENT' && t.status === 'COMPLETED')
      .sort((a, b) => b.date.getTime() - a.date.getTime())[0];

    return {
      ...assessment,
      totalPaid,
      remainingBalance: Math.max(0, assessment.totalAssessment - totalPaid),
      officialReceiptNumber: latestPayment?.referenceId,
      paymentDate: latestPayment?.date.toISOString(),
    };
  }
}
