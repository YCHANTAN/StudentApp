// src/application/dtos/enrollment.dto.ts
import { z } from 'zod';

// Rules from ENROLLMENT_RULES.md applied here
export const CreateEnrollmentDto = z.object({
  studentId: z.string().min(1, "Student ID is required"),
  courseIds: z.array(z.string()).min(1),
  fullName: z.string().min(3, "Full Legal Name must be at least 3 characters"),
  studentIdNumber: z.string().regex(/^S\d{5}$/, "Student ID Number must follow format SXXXXX"),
  emailAddress: z.string().email("Invalid email address"),
  phoneNumber: z.string().min(10).max(12),
  emergencyContactName: z.string().min(1, "Contact Person Name is required"),
  relationship: z.string().min(1, "Relationship is required"),
  emergencyPhone: z.string().min(10).max(12),
  paymentMethod: z.enum(['Credit/Debit Card', 'Online Banking', 'Cash at Counter']),
});

export const UpdateEnrollmentDto = CreateEnrollmentDto.partial();

export const EnrollmentResponseDto = z.object({
  id: z.string().uuid(),
  studentId: z.string(),
  courseIds: z.array(z.string()),
  fullName: z.string(),
  studentIdNumber: z.string(),
  emailAddress: z.string().email(),
  phoneNumber: z.string(),
  emergencyContactName: z.string(),
  relationship: z.string(),
  emergencyPhone: z.string(),
  selectedCredits: z.number(),
  estimatedTuition: z.number(),
  status: z.enum(['Draft', 'Confirmed', 'Adjusted']),
  paymentMethod: z.string(),
  isPaid: z.boolean(),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type CreateEnrollmentInput = z.infer<typeof CreateEnrollmentDto>;
export type UpdateEnrollmentInput = z.infer<typeof UpdateEnrollmentDto>;
