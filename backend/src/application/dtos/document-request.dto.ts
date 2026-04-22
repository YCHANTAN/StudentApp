import { z } from 'zod';

export const CreateDocumentRequestDto = z.object({
  studentId: z.string().uuid("Valid Student ID is required"),
  documentType: z.enum(['TOR', 'GOOD_MORAL', 'COE'], {
    errorMap: () => ({ message: "Invalid document type requested" })
  }),
  purpose: z.string().min(5, "Please provide a valid purpose (e.g., Transfer, Board Exam)"),
  copies: z.coerce.number().int().min(1).max(10, "You can only request up to 10 copies at a time"),
  notes: z.string().optional(),
});

export type CreateDocumentRequestInput = z.infer<typeof CreateDocumentRequestDto>;