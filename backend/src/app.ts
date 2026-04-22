import express from "express";

import { authRouter } from "@/presentation/routes/auth.routes";
import { studentRouter } from "@/presentation/routes/student.routes";
import { docsRouter } from "@/presentation/routes/docs.routes";
import { errorMiddleware } from "@/presentation/middleware/error.middleware";
import { bookRouter } from "@/presentation/routes/book.routes";
import { documentRouter } from "@/presentation/routes/document.routes";
import { financeRouter } from "@/presentation/routes/finance.routes";
import { programRouter } from '@/presentation/routes/program.routes';

export const app = express();

app.use(express.json());

app.use("/api", docsRouter);
app.use("/api/v1/auth", authRouter);
app.use("/api/v1/students", studentRouter);
app.use('/api/v1/books', bookRouter);
app.use('/api/v1/documents', documentRouter);
app.use('/api/v1/finance', financeRouter);
app.use('/api/v1/programs', programRouter);

app.use(errorMiddleware);
