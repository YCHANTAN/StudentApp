export const openApiSpec = {
  openapi: "3.0.3",
  info: {
    title: "StudentApp API",
    version: "1.0.0",
    description: "Clean Architecture backend API for Student resources.",
  },
  servers: [
    {
      url: "http://localhost:3000",
      description: "Local development",
    },
  ],
  tags: [
    {
      name: "Students",
      description: "Student management endpoints",
    },
  ],
  paths: {
    "/api/v1/students": {
      post: {
        tags: ["Students"],
        summary: "Create a student",
        requestBody: {
          required: true,
          content: {
            "application/json": {
              schema: { $ref: "#/components/schemas/CreateStudentRequest" },
            },
          },
        },
        responses: {
          "201": {
            description: "Student created",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/StudentResponse" },
              },
            },
          },
          "400": {
            description: "Validation error",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/ErrorResponse" },
              },
            },
          },
          "409": {
            description: "Conflict error",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/ErrorResponse" },
              },
            },
          },
        },
      },
    },
    "/api/v1/students/{id}": {
      get: {
        tags: ["Students"],
        summary: "Get a student by id",
        parameters: [
          {
            name: "id",
            in: "path",
            required: true,
            schema: { type: "string" },
          },
        ],
        responses: {
          "200": {
            description: "Student found",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/StudentResponse" },
              },
            },
          },
          "404": {
            description: "Student not found",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/ErrorResponse" },
              },
            },
          },
        },
      },
      patch: {
        tags: ["Students"],
        summary: "Update a student",
        parameters: [
          {
            name: "id",
            in: "path",
            required: true,
            schema: { type: "string" },
          },
        ],
        requestBody: {
          required: true,
          content: {
            "application/json": {
              schema: { $ref: "#/components/schemas/UpdateStudentRequest" },
            },
          },
        },
        responses: {
          "200": {
            description: "Student updated",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/StudentResponse" },
              },
            },
          },
          "400": {
            description: "Validation error",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/ErrorResponse" },
              },
            },
          },
          "404": {
            description: "Student not found",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/ErrorResponse" },
              },
            },
          },
        },
      },
      delete: {
        tags: ["Students"],
        summary: "Delete a student",
        parameters: [
          {
            name: "id",
            in: "path",
            required: true,
            schema: { type: "string" },
          },
        ],
        responses: {
          "204": {
            description: "Student deleted",
          },
          "404": {
            description: "Student not found",
            content: {
              "application/json": {
                schema: { $ref: "#/components/schemas/ErrorResponse" },
              },
            },
          },
        },
      },
    },
  },
  components: {
    schemas: {
      Student: {
        type: "object",
        properties: {
          id: { type: "string" },
          studentId: { type: "string" },
          fullName: { type: "string" },
          email: { type: "string", format: "email" },
          createdAt: { type: "string", format: "date-time" },
          updatedAt: { type: "string", format: "date-time" },
        },
        required: ["id", "studentId", "fullName", "email", "createdAt", "updatedAt"],
      },
      StudentResponse: {
        type: "object",
        properties: {
          success: { type: "boolean" },
          data: { $ref: "#/components/schemas/Student" },
        },
        required: ["success", "data"],
      },
      CreateStudentRequest: {
        type: "object",
        properties: {
          studentId: { type: "string", minLength: 1 },
          fullName: { type: "string", minLength: 1 },
          email: { type: "string", format: "email" },
        },
        required: ["studentId", "fullName", "email"],
      },
      UpdateStudentRequest: {
        type: "object",
        properties: {
          fullName: { type: "string", minLength: 1 },
          email: { type: "string", format: "email" },
        },
      },
      ErrorResponse: {
        type: "object",
        properties: {
          success: { type: "boolean" },
          error: {
            type: "object",
            properties: {
              code: { type: "string" },
              message: { type: "string" },
            },
            required: ["code", "message"],
          },
        },
        required: ["success", "error"],
      },
    },
  },
} as const;
