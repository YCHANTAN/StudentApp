import { db } from "./src/infrastructure/db/client.js";
import { studentProfiles } from "./src/infrastructure/db/schema/student-profile.schema.js";
import { eq } from "drizzle-orm";

async function checkProfile() {
  const result = await db.select().from(studentProfiles).where(eq(studentProfiles.id, "user_123"));
  console.log(JSON.stringify(result, null, 2));
  process.exit(0);
}

checkProfile().catch(console.error);
