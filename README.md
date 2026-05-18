# StudentApp — College Management System

A comprehensive, full-stack college management system designed to streamline student services, academic tracking, and library operations. This project consists of a high-performance **Node.js/TypeScript Backend** and a modern **Kotlin/Jetpack Compose Android Application**.

## 🌟 Key Features

### 📚 Advanced Library System
- **Real-time Inventory:** Automatically synchronizes available copies, stock labels, and status (Available/Limited/Out of Stock).
- **Borrowing Lifecycle:** Seamlessly track books through *Available*, *Borrowed (Return)*, and *History* states.
- **Personalized History:** Users can view their own borrow records and return status in real-time.
- **Stable Navigation:** Optimized backend queries ensure book positions remain consistent during list updates.

### 🎓 Academic & Enrollment
- **Digital Enrollment:** Select courses, validate prerequisites, and manage study loads.
- **Schedule Management:** Real-time view of class schedules, instructors, and room assignments.
- **Grade Tracking:** Complete history of academic performance with semester-wise filtering.
- **Course Evaluations:** Submit feedback on teaching quality and course materials.

### 💳 Finance & Services
- **Transaction History:** View all fees, payments, and balances with real-time status updates.
- **Document Requests:** Digital requests for TOR, Good Moral, and other academic certificates.
- **Complaint System:** Submit and track campus-related concerns directly through the app.

---

## 🛠️ Tech Stack

### Backend (Core API)
- **Runtime:** Node.js + TypeScript
- **Framework:** Express.js
- **Database:** PostgreSQL with **Drizzle ORM**
- **Architecture:** Clean Architecture (Domain-Driven Design)
- **Validation:** Zod
- **Documentation:** Swagger/OpenAPI

### Frontend (Android App)
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Networking:** Retrofit + OkHttp
- **Asynchronous:** Kotlin Coroutines
- **Image Loading:** Coil

---

## 🚀 Getting Started

### Backend Setup
1. Navigate to the `backend` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Create a `.env` file based on `.env.example` and configure your `DATABASE_URL`.
4. Run migrations and seed the database:
   ```bash
   npm run db:generate
   npm run db:migrate
   npm run seed
   ```
5. Start the development server:
   ```bash
   npm run dev
   ```

### Android Setup
1. Open the project in **Android Studio**.
2. Ensure the `BASE_URL` in `NetworkModule.kt` points to your machine's IP address (default is `10.0.2.2` for the emulator).
3. Build and run the app on an emulator or physical device.

---

## 📁 Project Structure

```text
StudentApp/
├── app/                  # Android Application (Kotlin/Compose)
│   └── src/main/java/    # Core logic, UI screens, and networking
├── backend/              # Node.js API (TypeScript/Drizzle)
│   ├── src/core/         # Domain entities and errors
│   ├── src/application/  # Use cases and repository interfaces
│   ├── src/infrastructure/# DB schema and repositories (Drizzle)
│   └── src/presentation/ # Express controllers and routes
└── documents/            # Design assets and project documentation
```

## 📜 License
This project is for educational purposes. All rights reserved.
