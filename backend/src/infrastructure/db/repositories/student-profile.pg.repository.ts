import { eq } from "drizzle-orm";
import type { NodePgDatabase } from "drizzle-orm/node-postgres";

import type { StudentProfileRepository } from "@/application/repositories/student-profile.repository";
import type { StudentProfile } from "@/core/entities/student-profile.entity";
import {
  type StudentProfileRow,
  studentProfiles,
} from "@/infrastructure/db/schema/student-profile.schema";

export class StudentProfilePgRepository implements StudentProfileRepository {
  constructor(private readonly db: NodePgDatabase) {}

  private toEntity(row: StudentProfileRow): StudentProfile {
    return {
      id: row.id,
      fullName: row.fullName,
      emailAddress: row.emailAddress,
      phoneNumber: row.phoneNumber,
      accountLabel: row.accountLabel,
      programSummary: row.programSummary,
      twoFactorStatus: row.twoFactorStatus as StudentProfile["twoFactorStatus"],
      emergencyContactName: row.emergencyContactName,
      emergencyContactRelationship: row.emergencyContactRelationship,
      emergencyContactPhoneNumber: row.emergencyContactPhoneNumber,
      emailNotifications: row.emailNotifications,
      smsNotifications: row.smsNotifications,
      systemAlerts: row.systemAlerts,
      createdAt: row.createdAt,
      updatedAt: row.updatedAt,
    };
  }

  async findById(id: string): Promise<StudentProfile | null> {
    const [row] = await this.db.select().from(studentProfiles).where(eq(studentProfiles.id, id));
    return row ? this.toEntity(row) : null;
  }

  async findAll(): Promise<StudentProfile[]> {
    const rows = await this.db.select().from(studentProfiles);
    return rows.map((row) => this.toEntity(row));
  }

  async update(id: string, patch: Partial<StudentProfile>): Promise<StudentProfile> {
    const [row] = await this.db
      .update(studentProfiles)
      .set(patch)
      .where(eq(studentProfiles.id, id))
      .returning();

    if (!row) {
      throw new Error("Failed to update student profile");
    }

    return this.toEntity(row);
  }
}
