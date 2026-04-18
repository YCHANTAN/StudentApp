import { Router } from "express";
import swaggerUi from "swagger-ui-express";

import { openApiSpec } from "@/presentation/docs/openapi";

export const docsRouter = Router();

docsRouter.get("/docs.json", (_req, res) => {
  res.status(200).json(openApiSpec);
});

docsRouter.use("/docs", swaggerUi.serve, swaggerUi.setup(openApiSpec));
