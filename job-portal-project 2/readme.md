# Simple JavaFX Job Portal (MySQL)

This is a **minimal, intermediate‑level JavaFX job portal** built on top of your original project.

It includes:

- JavaFX UI (single window, 3 tabs)
- MySQL database (users, jobs, applications)
- Login / Logout
- Register as **Employee** or **Employer**
- Employees:
  - Browse & search jobs
  - View job details
  - Apply for jobs
- Employers:
  - Post new jobs
  - Delete jobs they posted
- Profile tab:
  - Edit your name, location, skills & about section

---

## 1. MySQL Setup (once)

1. Start MySQL on your Mac.
2. Create a database:

   ```sql
   CREATE DATABASE job_portal_db;
   ```

3. Open `src/main/java/com/jobportal/Database.java` and update these constants:

   ```java
   private static final String DB_URL =
       "jdbc:mysql://localhost:3306/job_portal_db?useSSL=false&serverTimezone=UTC";
   private static final String DB_USER = "root";      // your MySQL username
   private static final String DB_PASSWORD = "password"; // your MySQL password
   ```

4. On first run, the app will automatically create tables:

   - `users`
   - `jobs`
   - `applications`

   It will also insert a few sample jobs if the `jobs` table is empty.

---

## 2. Build & Run with Maven (recommended)

You need:

- Java 21 (or adjust `pom.xml` to your Java version)
- Maven
- JavaFX 21 libraries (Maven already pulls them)

From the `job-portal-project` folder:

```bash
mvn clean javafx:run
```

The main class is `com.jobportal.Main`.

---

## 3. How to Use the App

### Register

1. In the **top login bar**, fill:
   - **Name**
   - **Email**
   - **Password**
   - **Role** = Employee or Employer
2. Click **Register**.
3. If registration is successful, you will see a message.

### Login

1. Enter your **email** and **password** (Name is optional for login).
2. Click **Login**.
3. Your name and role will appear on the right side of the header.

### Browse Jobs

- Use the **Browse Jobs** tab (default).
- Search using the search box at the top.
- Select a job in the table to see details on the right.

### Apply for a Job (Employee)

- Log in as **Employee**.
- Select a job.
- Click **Apply**.
- A record will be stored in the `applications` table.

### Post a Job (Employer)

- Log in as **Employer**.
- Go to the **Post Job** tab.
- Fill all fields and click **Post Job**.
- The job will appear in the Browse Jobs table.
- You can delete a job (only the ones you posted) from the Browse Jobs tab using **Delete Selected**.

### Profile

- After login, go to the **Profile** tab.
- Update:
  - Name
  - Location
  - Skills
  - About
- Click **Save Profile** to store changes in the database.

---

## 4. Notes for Your Class

- Passwords are stored **plain‑text** for simplicity (you can mention this as a possible future improvement).
- JDBC is used directly (no frameworks), which keeps the code easy to follow.
- UI is kept clean and modern‑looking but not overly complex:
  - one main window
  - top login bar
  - three tabs

You can further customise the UI and add features like:
- viewing applications per job,
- password hashing,
- input validation popups,
- pagination for job listings.

But as provided, this project should compile and run as a **fully working intermediate‑level job portal**.
