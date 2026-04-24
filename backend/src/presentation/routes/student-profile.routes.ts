import { Router } from "express";
import { UpdateStudentProfileDto } from "@/application/dtos/student-profile.dto";
import { studentProfileController } from "@/container";
import { authMiddleware } from "@/presentation/middleware/auth.middleware";
import { validate } from "@/presentation/middleware/validate.middleware";

export const studentProfileRouter = Router();

studentProfileRouter.get("/:id", studentProfileController.getStudentProfile);
studentProfileRouter.put(
  "/:id",
  authMiddleware,
  validate(UpdateStudentProfileDto),
  studentProfileController.updateStudentProfile,
);
