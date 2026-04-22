// src/presentation/lib/response.helper.ts
import type { Response } from 'express';

type Meta = {
  total: number;
  page: number;
  limit: number;
  totalPages: number;
};

export const ok = <T>(res: Response, data: T, meta?: Meta) => {
  return res.status(200).json({ success: true, data, ...(meta && { meta }) });
};

export const created = <T>(res: Response, data: T) => {
  return res.status(201).json({ success: true, data });
};

// API_STANDARDS explicitly states to use 200 with data: null for DELETE, not 204.
export const noData = (res: Response) => {
  return res.status(200).json({ success: true, data: null });
};

export const badRequest = (res: Response, message: string, details?: unknown) => {
  return res.status(400).json({
    success: false,
    error: { 
      code: 'BAD_REQUEST', 
      message, 
      ...(details !== undefined ? { details } : {}) 
    },
  });
};