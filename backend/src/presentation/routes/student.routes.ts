import { Router } from "express";

import { CreateStudentDto, UpdateStudentDto } from "@/application/dtos/student.dto";
import { studentController } from "@/container";
import { authMiddleware } from "@/presentation/middleware/auth.middleware";
import { validate } from "@/presentation/middleware/validate.middleware";

export const studentRouter = Router();

studentRouter.post("/", authMiddleware, validate(CreateStudentDto), studentController.create);
studentRouter.get("/:id", studentController.findById);
studentRouter.patch("/:id", authMiddleware, validate(UpdateStudentDto), studentController.update);
studentRouter.delete("/:id", authMiddleware, studentController.remove);
