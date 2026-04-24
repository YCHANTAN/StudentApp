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
    { name: "Auth", description: "Authentication" },
    { name: "Students", description: "Student Profiles" },
    { name: "Programs", description: "Academic Programs" },
    { name: "Courses", description: "Course Catalog" },
    { name: "Library", description: "Library Books" },
    { name: "Finance", description: "Financial Transactions" },
    { name: "Grades", description: "Grade Records" },
    { name: "Schedule", description: "Class Schedules" },
    { name: "Complaints", description: "Student Complaints" },
    { name: "Documents", description: "Document Requests" },
    { name: "Enrollments", description: "Course Enrollments" },
  ],
  paths: {
    "/api/v1/auth/login": {
      post: {
        tags: ["Auth"],
        summary: "Login",
        requestBody: {
          required: true,
          content: { "application/json": { schema: { $ref: "#/components/schemas/LoginRequest" } } },
        },
        responses: {
          "200": { description: "Success", content: { "application/json": { schema: { $ref: "#/components/schemas/LoginResponse" } } } }
        },
      },
    },
    "/api/v1/auth/me": {
      get: {
        tags: ["Auth"],
        summary: "Get current user info",
        security: [{ BearerAuth: [] }],
        responses: {
          "200": { description: "Success" }
        },
      },
    },
    "/api/v1/students/{id}": {
      get: {
        tags: ["Students"],
        summary: "Get profile",
        parameters: [{ name: "id", in: "path", required: true, schema: { type: "string" } }],
        responses: { "200": { description: "Success" } },
      },
      put: {
        tags: ["Students"],
        summary: "Update profile",
        security: [{ BearerAuth: [] }],
        parameters: [{ name: "id", in: "path", required: true, schema: { type: "string" } }],
        requestBody: { required: true, content: { "application/json": { schema: { $ref: "#/components/schemas/UpdateStudentProfileRequest" } } } },
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/programs": {
      get: {
        tags: ["Programs"],
        summary: "List programs",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "category", in: "query", schema: { type: "string", enum: ["Undergraduate", "Postgraduate"] } },
        ],
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/courses": {
      get: {
        tags: ["Courses"],
        summary: "List courses",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "programId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/library-books": {
      get: {
        tags: ["Library"],
        summary: "List books",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "tab", in: "query", schema: { type: "string", enum: ["Available", "Return", "History"] } },
        ],
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/transactions": {
      get: {
        tags: ["Finance"],
        summary: "List transactions",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "studentId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/grades": {
      get: {
        tags: ["Grades"],
        summary: "List grade records",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "studentId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/schedule": {
      get: {
        tags: ["Schedule"],
        summary: "List schedule entries",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "studentId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
    },
    "/api/v1/complaints": {
      get: {
        tags: ["Complaints"],
        summary: "List complaints",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "studentId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
      post: {
        tags: ["Complaints"],
        summary: "Create complaint",
        requestBody: { required: true, content: { "application/json": { schema: { $ref: "#/components/schemas/CreateComplaintRequest" } } } },
        responses: { "201": { description: "Created" } },
      },
    },
    "/api/v1/document-requests": {
      get: {
        tags: ["Documents"],
        summary: "List document requests",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "studentId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
      post: {
        tags: ["Documents"],
        summary: "Create document request",
        requestBody: { required: true, content: { "application/json": { schema: { $ref: "#/components/schemas/CreateDocumentRequest" } } } },
        responses: { "201": { description: "Created" } },
      },
    },
    "/api/v1/enrollments": {
      get: {
        tags: ["Enrollments"],
        summary: "List enrollments",
        parameters: [
          { name: "page", in: "query", schema: { type: "integer" } },
          { name: "limit", in: "query", schema: { type: "integer" } },
          { name: "studentId", in: "query", schema: { type: "string" } },
        ],
        responses: { "200": { description: "Success" } },
      },
      post: {
        tags: ["Enrollments"],
        summary: "Create enrollment",
        requestBody: { required: true, content: { "application/json": { schema: { $ref: "#/components/schemas/CreateEnrollmentRequest" } } } },
        responses: { "201": { description: "Created" } },
      },
    },
  },
  components: {
    securitySchemes: {
      BearerAuth: { type: "http", scheme: "bearer", bearerFormat: "JWT" },
    },
    schemas: {
      LoginRequest: {
        type: "object",
        properties: {
          studentId: { type: "string" },
          password: { type: "string" },
          keepLoggedIn: { type: "boolean" },
        },
        required: ["studentId", "password"],
      },
      LoginResponse: {
        type: "object",
        properties: {
          success: { type: "boolean" },
          data: {
            type: "object",
            properties: {
              accessToken: { type: "string" },
              tokenType: { type: "string" },
              expiresIn: { type: "string" },
            },
          },
        },
      },
      UpdateStudentProfileRequest: {
        type: "object",
        properties: {
          fullName: { type: "string" },
          emailAddress: { type: "string" },
          phoneNumber: { type: "string" },
        },
      },
      CreateComplaintRequest: {
        type: "object",
        properties: {
          studentId: { type: "string" },
          title: { type: "string" },
        },
        required: ["studentId", "title"],
      },
      CreateDocumentRequest: {
        type: "object",
        properties: {
          studentId: { type: "string" },
          type: { type: "string", enum: ["TOR", "GoodMoral", "COE"] },
          purpose: { type: "string" },
        },
        required: ["studentId", "type", "purpose"],
      },
      CreateEnrollmentRequest: {
        type: "object",
        properties: {
          studentId: { type: "string" },
          courseIds: { type: "array", items: { type: "string" } },
        },
        required: ["studentId", "courseIds"],
      },
    },
  },
} as const;
