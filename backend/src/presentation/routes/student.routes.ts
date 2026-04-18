import { Router } from "express";

import { CreateStudentDto, UpdateStudentDto } from "@/application/dtos/student.dto";
import { studentController } from "@/container";
import { validate } from "@/presentation/middleware/validate.middleware";

export const studentRouter = Router();

studentRouter.post("/", validate(CreateStudentDto), studentController.create);
studentRouter.get("/:id", studentController.findById);
studentRouter.patch("/:id", validate(UpdateStudentDto), studentController.update);
studentRouter.delete("/:id", studentController.remove);
