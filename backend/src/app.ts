import express from "express";

import { authRouter } from "@/presentation/routes/auth.routes";
import { studentProfileRouter } from "@/presentation/routes/student-profile.routes";
import { docsRouter } from "@/presentation/routes/docs.routes";
import { errorMiddleware } from "@/presentation/middleware/error.middleware";
import { libraryBookRouter } from "@/presentation/routes/library-book.routes";
import { documentRequestRouter } from "@/presentation/routes/document-request.routes";
import { complaintRouter } from "@/presentation/routes/complaint.routes";
import { financeRouter } from "@/presentation/routes/finance.routes";
import { programRouter } from '@/presentation/routes/program.routes';
import { courseRouter } from '@/presentation/routes/course.routes';
import { scheduleEntryRouter } from '@/presentation/routes/schedule-entry.routes';
import { gradeRecordRouter } from '@/presentation/routes/grade-record.routes';
import { transactionRouter } from '@/presentation/routes/transaction.routes';
import { enrollmentRouter } from '@/presentation/routes/enrollment.routes';
import { subjectRouter } from '@/presentation/routes/subject.routes';
import { globalRateLimiter } from "@/presentation/middleware/rate-limit.middleware";

export const app = express();

app.use(express.json());
app.use(globalRateLimiter);

app.use("/api", docsRouter);
app.use("/api/v1/auth", authRouter);
app.use("/api/v1/students", studentProfileRouter);
app.use('/api/v1/library-books', libraryBookRouter);
app.use('/api/v1/document-requests', documentRequestRouter);
app.use('/api/v1/complaints', complaintRouter);
app.use('/api/v1/finance', financeRouter);
app.use('/api/v1/programs', programRouter);
app.use('/api/v1/courses', courseRouter);
app.use('/api/v1/schedule', scheduleEntryRouter);
app.use('/api/v1/grades', gradeRecordRouter);
app.use('/api/v1/transactions', transactionRouter);
app.use('/api/v1/enrollments', enrollmentRouter);
app.use('/api/v1/subjects', subjectRouter);

app.use(errorMiddleware);
