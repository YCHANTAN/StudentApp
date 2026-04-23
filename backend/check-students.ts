import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { students } from "./src/infrastructure/db/schema/student.schema.js";

async function checkStudents() {
  const all = await db.select().from(students);
  console.log(all);
  process.exit(0);
}

checkStudents();
