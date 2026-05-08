import { db } from "./src/infrastructure/db/client.js";
import { transactions } from "./src/infrastructure/db/schema/transaction.schema.js";
import { eq } from "drizzle-orm";

async function listTransactions() {
  const studentId = "user_123";
  const result = await db.select().from(transactions).where(eq(transactions.studentId, studentId));
  console.log(JSON.stringify(result, null, 2));
  process.exit(0);
}

listTransactions().catch(console.error);
