import { Router } from "express";

import { UpdateStudentProfileDto } from "@/application/dtos/student-profile.dto";
import { studentProfileController, subjectRegistrationController } from "@/container";
import { authMiddleware } from "@/presentation/middleware/auth.middleware";
import { validate } from "@/presentation/middleware/validate.middleware";

export const studentRouter = Router();

studentRouter.get("/", studentProfileController.listStudents);
studentRouter.get("/:id", studentProfileController.getStudentProfile);
studentRouter.put(
  "/:id",
  authMiddleware,
  validate(UpdateStudentProfileDto),
  studentProfileController.updateStudentProfile,
);

// Subject Registration Routes
studentRouter.get("/:id/subjects/enrolled", subjectRegistrationController.getEnrolledSubjects);
studentRouter.get("/:id/subjects/completed", subjectRegistrationController.getCompletedSubjects);
studentRouter.get("/:id/subjects/waitlisted", subjectRegistrationController.getWaitlistedSubjects);
