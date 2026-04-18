import express from "express";

import { studentRouter } from "@/presentation/routes/student.routes";
import { docsRouter } from "@/presentation/routes/docs.routes";
import { errorMiddleware } from "@/presentation/middleware/error.middleware";

export const app = express();

app.use(express.json());

app.use("/api", docsRouter);
app.use("/api/v1/students", studentRouter);

app.use(errorMiddleware);
