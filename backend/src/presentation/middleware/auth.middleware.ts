import type { NextFunction, Request, Response } from "express";
import jwt from "jsonwebtoken";
import 'dotenv/config'

const JWT_SECRET = process.env.JWT_SECRET || 'fallback_secret_do_not_use_in_production';

export const requireAuth = (req: Request, res: Response, next: NextFunction): void => {
  const authHeader = req.headers['authorization'];
  
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    res.status(401).json({ success: false, error: 'Access Denied. No token provided.' });
    return;
  }

  jwt.verify(token, JWT_SECRET, (err, decodedUser) => {
    if (err) {
      res.status(403).json({ success: false, error: 'Invalid or Expired Token.' });
      return;
    }
    
    (req as any).user = decodedUser; 
    
    next(); 
  });
};