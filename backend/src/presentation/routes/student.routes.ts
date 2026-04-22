import { Router } from "express";

import { CreateStudentDto, UpdateStudentDto } from "@/application/dtos/student.dto";
import { studentController, registrationController } from "@/container";
import { validate } from "@/presentation/middleware/validate.middleware";

export const studentRouter = Router();

studentRouter.post("/", validate(CreateStudentDto), studentController.create);
studentRouter.get("/:id", studentController.findById);
studentRouter.patch("/:id", validate(UpdateStudentDto), studentController.update);
studentRouter.delete("/:id", studentController.remove);

studentRouter.get('/:id/subjects/enrolled', registrationController.getEnrolledSubjects);
studentRouter.get('/:id/subjects/completed', registrationController.getCompletedSubjects);
studentRouter.get('/:id/subjects/waitlisted', registrationController.getWaitlistedSubjects);
