CREATE TYPE "public"."complaint_status" AS ENUM('IN_REVIEW', 'RESOLVED');--> statement-breakpoint
CREATE TYPE "public"."delivery_method" AS ENUM('Pickup', 'Courier');--> statement-breakpoint
CREATE TYPE "public"."document_request_status" AS ENUM('PROCESSING', 'ACCEPTED', 'READY_FOR_PICKUP');--> statement-breakpoint
CREATE TYPE "public"."library_book_tab" AS ENUM('Available', 'Return', 'History');--> statement-breakpoint
CREATE TYPE "public"."stock_status" AS ENUM('Available', 'Limited', 'OutOfStock');--> statement-breakpoint
CREATE TABLE "complaints" (
	"id" uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
	"student_id" text NOT NULL,
	"title" varchar(255) NOT NULL,
	"status" "complaint_status" NOT NULL,
	"created_at" timestamp DEFAULT now() NOT NULL,
	"updated_at" timestamp DEFAULT now() NOT NULL
);
--> statement-breakpoint
CREATE TABLE "courses" (
	"id" uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
	"code" varchar(50) NOT NULL,
	"title" varchar(255) NOT NULL,
	"semester_title" varchar(100),
	"instructor" varchar(255),
	"units" integer,
	"schedule" varchar(255),
	"location" varchar(255),
	"grade" varchar(10),
	"waitlist_status" varchar(100),
	"progress" numeric(3, 2),
	"status" varchar(50),
	"tuition" numeric(10, 2),
	"is_locked" boolean DEFAULT false,
	"lock_reason" text,
	"program_id" varchar(128),
	"created_at" timestamp DEFAULT now() NOT NULL,
	CONSTRAINT "courses_code_unique" UNIQUE("code")
);
--> statement-breakpoint
CREATE TABLE "enrollment_courses" (
	"enrollment_id" uuid NOT NULL,
	"course_id" uuid NOT NULL
);
--> statement-breakpoint
CREATE TABLE "grade_records" (
	"id" uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
	"student_id" text NOT NULL,
	"title" varchar(255) NOT NULL,
	"code_credits" varchar(100) NOT NULL,
	"grade_point" varchar(20) NOT NULL,
	"status" varchar(50) NOT NULL,
	"semester_label" varchar(100),
	"created_at" timestamp DEFAULT now() NOT NULL
);
--> statement-breakpoint
CREATE TABLE "library_books" (
	"id" varchar(128) PRIMARY KEY NOT NULL,
	"title" varchar(255) NOT NULL,
	"author" varchar(255) NOT NULL,
	"rating" double precision NOT NULL,
	"genre" varchar(100) NOT NULL,
	"stock_label" varchar(100) NOT NULL,
	"stock_status" "stock_status" NOT NULL,
	"available_copies" integer DEFAULT 0 NOT NULL,
	"is_new" boolean DEFAULT false NOT NULL,
	"tab" "library_book_tab" NOT NULL,
	"created_at" timestamp DEFAULT now() NOT NULL
);
--> statement-breakpoint
CREATE TABLE "schedule_entries" (
	"id" uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
	"student_id" text NOT NULL,
	"day_label" varchar(50) NOT NULL,
	"course_code" varchar(50) NOT NULL,
	"course_title" varchar(255) NOT NULL,
	"time_range" varchar(100) NOT NULL,
	"room" varchar(100) NOT NULL,
	"instructor" varchar(255) NOT NULL,
	"created_at" timestamp DEFAULT now() NOT NULL
);
--> statement-breakpoint
ALTER TABLE "books" DISABLE ROW LEVEL SECURITY;--> statement-breakpoint
ALTER TABLE "enrollment_subjects" DISABLE ROW LEVEL SECURITY;--> statement-breakpoint
DROP TABLE "books" CASCADE;--> statement-breakpoint
DROP TABLE "enrollment_subjects" CASCADE;--> statement-breakpoint
ALTER TABLE "borrow_records" DROP CONSTRAINT "borrow_records_book_id_books_id_fk";
--> statement-breakpoint
ALTER TABLE "document_requests" DROP CONSTRAINT "document_requests_student_id_students_id_fk";
--> statement-breakpoint
ALTER TABLE "transactions" DROP CONSTRAINT "transactions_student_id_students_id_fk";
--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "type" SET DATA TYPE text;--> statement-breakpoint
DROP TYPE "public"."document_type";--> statement-breakpoint
CREATE TYPE "public"."document_type" AS ENUM('TOR', 'GoodMoral', 'COE');--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "type" SET DATA TYPE "public"."document_type" USING "type"::"public"."document_type";--> statement-breakpoint
ALTER TABLE "enrollments" ALTER COLUMN "status" SET DATA TYPE text;--> statement-breakpoint
ALTER TABLE "enrollments" ALTER COLUMN "status" SET DEFAULT 'PENDING'::text;--> statement-breakpoint
DROP TYPE "public"."enrollment_status";--> statement-breakpoint
CREATE TYPE "public"."enrollment_status" AS ENUM('PENDING', 'APPROVED', 'REJECTED');--> statement-breakpoint
ALTER TABLE "enrollments" ALTER COLUMN "status" SET DEFAULT 'PENDING'::"public"."enrollment_status";--> statement-breakpoint
ALTER TABLE "enrollments" ALTER COLUMN "status" SET DATA TYPE "public"."enrollment_status" USING "status"::"public"."enrollment_status";--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "type" SET DATA TYPE text;--> statement-breakpoint
DROP TYPE "public"."transaction_type";--> statement-breakpoint
CREATE TYPE "public"."transaction_type" AS ENUM('PAYMENT', 'FEE', 'REFUND');--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "type" SET DATA TYPE "public"."transaction_type" USING "type"::"public"."transaction_type";--> statement-breakpoint
ALTER TABLE "borrow_records" ALTER COLUMN "book_id" SET DATA TYPE varchar(128);--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "id" SET DATA TYPE uuid;--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "id" SET DEFAULT gen_random_uuid();--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "copies" DROP DEFAULT;--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "copies" DROP NOT NULL;--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "updated_at" SET DATA TYPE timestamp;--> statement-breakpoint
ALTER TABLE "document_requests" ALTER COLUMN "updated_at" SET DEFAULT now();--> statement-breakpoint
ALTER TABLE "enrollments" ALTER COLUMN "student_id" SET DATA TYPE text;--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "id" SET DATA TYPE uuid;--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "id" SET DEFAULT gen_random_uuid();--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "amount" SET DATA TYPE varchar(50);--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "reference_id" SET DATA TYPE varchar(50);--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "description" DROP NOT NULL;--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "created_at" SET DATA TYPE timestamp;--> statement-breakpoint
ALTER TABLE "transactions" ALTER COLUMN "created_at" SET DEFAULT now();--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "type" "document_type" NOT NULL;--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "program" varchar(255);--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "year_level" varchar(50);--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "delivery_method" "delivery_method";--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "reference" varchar(50) NOT NULL;--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "status" "document_request_status" NOT NULL;--> statement-breakpoint
ALTER TABLE "document_requests" ADD COLUMN "submitted_at" timestamp DEFAULT now() NOT NULL;--> statement-breakpoint
ALTER TABLE "enrollments" ADD COLUMN "total_units" integer NOT NULL;--> statement-breakpoint
ALTER TABLE "enrollments" ADD COLUMN "total_tuition" numeric(12, 2) NOT NULL;--> statement-breakpoint
ALTER TABLE "programs" ADD COLUMN "prospectus_url" text;--> statement-breakpoint
ALTER TABLE "transactions" ADD COLUMN "title" varchar(255) NOT NULL;--> statement-breakpoint
ALTER TABLE "transactions" ADD COLUMN "type" "transaction_type" NOT NULL;--> statement-breakpoint
ALTER TABLE "transactions" ADD COLUMN "method" varchar(100) NOT NULL;--> statement-breakpoint
ALTER TABLE "transactions" ADD COLUMN "status" "transaction_status" NOT NULL;--> statement-breakpoint
ALTER TABLE "transactions" ADD COLUMN "date" timestamp DEFAULT now() NOT NULL;--> statement-breakpoint
ALTER TABLE "transactions" ADD COLUMN "is_paid" boolean DEFAULT false NOT NULL;--> statement-breakpoint
ALTER TABLE "complaints" ADD CONSTRAINT "complaints_student_id_student_profiles_id_fk" FOREIGN KEY ("student_id") REFERENCES "public"."student_profiles"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "courses" ADD CONSTRAINT "courses_program_id_programs_id_fk" FOREIGN KEY ("program_id") REFERENCES "public"."programs"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "enrollment_courses" ADD CONSTRAINT "enrollment_courses_enrollment_id_enrollments_id_fk" FOREIGN KEY ("enrollment_id") REFERENCES "public"."enrollments"("id") ON DELETE cascade ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "enrollment_courses" ADD CONSTRAINT "enrollment_courses_course_id_courses_id_fk" FOREIGN KEY ("course_id") REFERENCES "public"."courses"("id") ON DELETE cascade ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "grade_records" ADD CONSTRAINT "grade_records_student_id_student_profiles_id_fk" FOREIGN KEY ("student_id") REFERENCES "public"."student_profiles"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "schedule_entries" ADD CONSTRAINT "schedule_entries_student_id_student_profiles_id_fk" FOREIGN KEY ("student_id") REFERENCES "public"."student_profiles"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "borrow_records" ADD CONSTRAINT "borrow_records_book_id_library_books_id_fk" FOREIGN KEY ("book_id") REFERENCES "public"."library_books"("id") ON DELETE cascade ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "document_requests" ADD CONSTRAINT "document_requests_student_id_student_profiles_id_fk" FOREIGN KEY ("student_id") REFERENCES "public"."student_profiles"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "enrollments" ADD CONSTRAINT "enrollments_student_id_student_profiles_id_fk" FOREIGN KEY ("student_id") REFERENCES "public"."student_profiles"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "transactions" ADD CONSTRAINT "transactions_student_id_student_profiles_id_fk" FOREIGN KEY ("student_id") REFERENCES "public"."student_profiles"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "document_requests" DROP COLUMN "document_type";--> statement-breakpoint
ALTER TABLE "document_requests" DROP COLUMN "request_status";--> statement-breakpoint
ALTER TABLE "document_requests" DROP COLUMN "notes";--> statement-breakpoint
ALTER TABLE "document_requests" DROP COLUMN "requested_at";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "full_name";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "student_id_number";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "email_address";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "phone_number";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "emergency_contact_name";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "relationship";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "emergency_phone";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "selected_credits";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "estimated_tuition";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "payment_method";--> statement-breakpoint
ALTER TABLE "enrollments" DROP COLUMN "is_paid";--> statement-breakpoint
ALTER TABLE "transactions" DROP COLUMN "transaction_type";--> statement-breakpoint
ALTER TABLE "transactions" DROP COLUMN "payment_method";--> statement-breakpoint
ALTER TABLE "transactions" DROP COLUMN "transaction_status";--> statement-breakpoint
ALTER TABLE "document_requests" ADD CONSTRAINT "document_requests_reference_unique" UNIQUE("reference");--> statement-breakpoint
ALTER TABLE "transactions" ADD CONSTRAINT "transactions_reference_id_unique" UNIQUE("reference_id");--> statement-breakpoint
DROP TYPE "public"."request_status";--> statement-breakpoint
DROP TYPE "public"."payment_method";