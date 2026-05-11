import { z } from "zod";

export const AssessmentDto = z.object({
  studentName: z.string(),
  studentId: z.string(),
  program: z.string(),
  semester: z.string(),
  schoolYear: z.string(),
  subjects: z.array(z.object({
    code: z.string(),
    title: z.string(),
    units: z.number(),
    tuition: z.number(),
  })),
  totalUnits: z.number(),
  totalTuition: z.number(),
  miscellaneousFees: z.array(z.object({
    description: z.string(),
    amount: z.number(),
  })),
  totalAssessment: z.number(),
});

export const PaymentSlipDto = AssessmentDto.extend({
  totalPaid: z.number(),
  remainingBalance: z.number(),
  officialReceiptNumber: z.string().optional(),
  paymentDate: z.string().optional(),
});

export type Assessment = z.infer<typeof AssessmentDto>;
export type PaymentSlip = z.infer<typeof PaymentSlipDto>;
