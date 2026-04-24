import { z } from 'zod';

export const DocumentTypeDto = z.enum(['TOR', 'GoodMoral', 'COE']);
export const DeliveryMethodDto = z.enum(['Pickup', 'Courier']);
export const DocumentRequestStatusDto = z.enum(['PROCESSING', 'ACCEPTED', 'READY_FOR_PICKUP']);

export const CreateDocumentRequestDto = z.object({
  studentId: z.string(),
  type: DocumentTypeDto,
  purpose: z.string(),
  program: z.string().optional(),
  yearLevel: z.string().optional(),
  copies: z.number().int().min(1).optional(),
  deliveryMethod: DeliveryMethodDto.optional(),
});

export const UpdateDocumentRequestDto = CreateDocumentRequestDto.partial();

export const DocumentRequestResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  type: DocumentTypeDto,
  purpose: z.string(),
  program: z.string().optional(),
  yearLevel: z.string().optional(),
  copies: z.number().int().optional(),
  deliveryMethod: DeliveryMethodDto.optional(),
  reference: z.string(),
  status: DocumentRequestStatusDto,
  submittedAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type CreateDocumentRequestInput = z.infer<typeof CreateDocumentRequestDto>;
export type UpdateDocumentRequestInput = z.infer<typeof UpdateDocumentRequestDto>;

export const GetDocumentRequestsQueryDto = z.object({
  studentId: z.string().optional(),
});
