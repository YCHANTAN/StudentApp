import type { Request, Response } from 'express';
import type{ User } from '../auth/auth.models';

export interface JwtPayLoad {
    id: string;
    username: string;
    email: string;
}
export interface AuthenticatedRequest extends Request {
    user?: JwtPayLoad;
}