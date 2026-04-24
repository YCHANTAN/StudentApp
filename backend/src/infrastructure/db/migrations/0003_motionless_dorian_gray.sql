CREATE TYPE "public"."enrollment_status" AS ENUM('Draft', 'Confirmed', 'Adjusted');--> statement-breakpoint
CREATE TABLE "enrollment_subjects" (
	"enrollment_id" uuid NOT NULL,
	"subject_id" varchar(128) NOT NULL
);
--> statement-breakpoint
CREATE TABLE "enrollments" (
	"id" uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
	"student_id" varchar(128) NOT NULL,
	"full_name" varchar(255) NOT NULL,
	"student_id_number" varchar(128) NOT NULL,
	"email_address" varchar(255) NOT NULL,
	"phone_number" varchar(50) NOT NULL,
	"emergency_contact_name" varchar(255) NOT NULL,
	"relationship" varchar(100) NOT NULL,
	"emergency_phone" varchar(50) NOT NULL,
	"selected_credits" integer NOT NULL,
	"estimated_tuition" numeric(12, 2) NOT NULL,
	"status" "enrollment_status" DEFAULT 'Draft' NOT NULL,
	"payment_method" varchar(100) NOT NULL,
	"is_paid" boolean DEFAULT false NOT NULL,
	"created_at" timestamp DEFAULT now() NOT NULL,
	"updated_at" timestamp DEFAULT now() NOT NULL
);
--> statement-breakpoint
ALTER TABLE "enrollment_subjects" ADD CONSTRAINT "enrollment_subjects_enrollment_id_enrollments_id_fk" FOREIGN KEY ("enrollment_id") REFERENCES "public"."enrollments"("id") ON DELETE no action ON UPDATE no action;--> statement-breakpoint
ALTER TABLE "enrollment_subjects" ADD CONSTRAINT "enrollment_subjects_subject_id_subjects_id_fk" FOREIGN KEY ("subject_id") REFERENCES "public"."subjects"("id") ON DELETE no action ON UPDATE no action;