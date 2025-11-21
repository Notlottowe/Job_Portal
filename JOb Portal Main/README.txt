Job Portal - Modern JavaFX (No FXML)
======================================

How to run
----------

1. Create MySQL database:

   CREATE DATABASE job_portal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

2. Create tables:

   See schema.sql in project root.

3. Update Database.java with your MySQL username/password.

4. From project folder run:

   mvn clean javafx:run

Login & roles
-------------

- Register as Employee or Employer from the Register screen.
- Employer can:
  - Post jobs
  - See their posted jobs
  - View applicants for a selected job
- Employee can:
  - Browse and search jobs
  - Apply once per job (duplicate applications are blocked)
  - Edit profile (bio, experience, etc.)
  - Post updates in Activity Feed (LinkedIn-style posts)
