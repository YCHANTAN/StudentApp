import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { programs } from "./src/infrastructure/db/schema/program.schema.js";

async function checkPrograms() {
  const all = await db.select().from(programs);
  console.log("Current Programs in DB:", JSON.stringify(all, null, 2));
  process.exit(0);
}

checkPrograms();
