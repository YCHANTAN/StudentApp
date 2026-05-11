import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { courses } from "./src/infrastructure/db/schema/course.schema.js";

async function checkCourses() {
  const all = await db.select().from(courses);
  console.log(JSON.stringify(all, null, 2));
  process.exit(0);
}

checkCourses();
