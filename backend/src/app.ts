import express from "express";

import { authRouter } from "@/presentation/routes/auth.routes";
import { studentRouter } from "@/presentation/routes/student.routes";
import { docsRouter } from "@/presentation/routes/docs.routes";
import { errorMiddleware } from "@/presentation/middleware/error.middleware";
import { bookRouter } from "@/presentation/routes/book.routes";
import { documentRouter } from "@/presentation/routes/document.routes";
import { financeRouter } from "@/presentation/routes/finance.routes";
import { programRouter } from '@/presentation/routes/program.routes';
import { enrollmentRouter } from '@/presentation/routes/enrollment.routes';
import { subjectRouter } from '@/presentation/routes/subject.routes';
import { globalRateLimiter } from "@/presentation/middleware/rate-limit.middleware";

export const app = express();

app.use(express.json());
app.use(globalRateLimiter);

app.use("/api", docsRouter);
app.use("/api/v1/auth", authRouter);
app.use("/api/v1/students", studentRouter);
app.use('/api/v1/books', bookRouter);
app.use('/api/v1/documents', documentRouter);
app.use('/api/v1/finance', financeRouter);
app.use('/api/v1/programs', programRouter);
app.use('/api/v1/enrollments', enrollmentRouter);
app.use('/api/v1/subjects', subjectRouter);

app.use(errorMiddleware);
