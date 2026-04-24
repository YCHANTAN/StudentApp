CREATE TYPE "public"."badge_variant" AS ENUM('Accent', 'Primary');--> statement-breakpoint
ALTER TABLE "programs" ALTER COLUMN "description" SET NOT NULL;--> statement-breakpoint
ALTER TABLE "programs" ALTER COLUMN "created_at" SET NOT NULL;--> statement-breakpoint
ALTER TABLE "programs" ADD COLUMN "badge_text" varchar(100) NOT NULL;--> statement-breakpoint
ALTER TABLE "programs" ADD COLUMN "badge_variant" "badge_variant" NOT NULL;--> statement-breakpoint
ALTER TABLE "programs" ADD COLUMN "schedule_line" varchar(255) NOT NULL;