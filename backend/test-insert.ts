import "dotenv/config";
import { db } from "./src/infrastructure/db/client.js";
import { transactions } from "./src/infrastructure/db/schema/transaction.schema.js";
import { v4 as uuidv4 } from 'uuid';

async function testInsert() {
  try {
    const studentId = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11";
    const res = await db.insert(transactions).values({
      id: uuidv4(),
      studentId,
      type: "PAYMENT",
      amount: 500,
      method: "CASH",
      status: "COMPLETED",
      referenceId: "TEST-REF",
      description: "Test Insert",
      createdAt: new Date()
    }).returning();
    console.log("Success:", res);
  } catch (err) {
    console.error("Insert failed with error details:");
    console.error(err);
  }
  process.exit(0);
}

testInsert();
