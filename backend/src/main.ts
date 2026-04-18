import { app } from "@/app";
import { env } from "@/infrastructure/config/env";

app.listen(env.PORT, () => {
  console.log(`Server running on port ${env.PORT}`);
});
