import { z } from "zod";

/**
 * Request contract for logging a student into the API.
 */
export const LoginDto = z.object({
  studentId: z.string().min(1),
  password: z.string().min(1),
  keepLoggedIn: z.boolean().default(false),
});

export type LoginInput = z.infer<typeof LoginDto>;

/**
 * Response payload returned by the login use case.
 */
export type LoginResponse = {
  accessToken: string;
  tokenType: "Bearer";
  expiresIn: string;
  refreshToken?: string;
};
