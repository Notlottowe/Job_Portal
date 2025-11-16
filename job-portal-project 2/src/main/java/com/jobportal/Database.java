package com.jobportal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/job_portal_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";        // <-- change if needed
    private static final String DB_PASSWORD = "MeghP169"; // <-- change if needed

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ---------- USERS ----------

    public static boolean registerUser(User user) {
        String sql = "INSERT INTO users(full_name, email, password, role, birthdate, description, experience, education, skills) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            if (user.getBirthdate() != null) {
                ps.setDate(5, Date.valueOf(user.getBirthdate()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            ps.setString(6, user.getDescription());
            ps.setString(7, user.getExperience());
            ps.setString(8, user.getEducation());
            ps.setString(9, user.getSkills());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateUserProfile(User user) {
        String sql = "UPDATE users SET full_name=?, birthdate=?, description=?, " +
                     "experience=?, education=?, skills=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            if (user.getBirthdate() != null) {
                ps.setDate(2, Date.valueOf(user.getBirthdate()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setString(3, user.getDescription());
            ps.setString(4, user.getExperience());
            ps.setString(5, user.getEducation());
            ps.setString(6, user.getSkills());
            ps.setInt(7, user.getId());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getInt("id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("role")
        );

        Date bd = rs.getDate("birthdate");
        if (bd != null) {
            user.setBirthdate(bd.toLocalDate());
        }
        user.setDescription(rs.getString("description"));
        user.setExperience(rs.getString("experience"));
        user.setEducation(rs.getString("education"));
        user.setSkills(rs.getString("skills"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    // ---------- JOBS ----------

    public static boolean addJob(Job job) {
        String sql = "INSERT INTO jobs(employer_id, title, company, description, location, salary, type) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, job.getEmployerId());
            ps.setString(2, job.getTitle());
            ps.setString(3, job.getCompany());
            ps.setString(4, job.getDescription());
            ps.setString(5, job.getLocation());
            ps.setDouble(6, job.getSalary());
            ps.setString(7, job.getType());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Job> getAllJobs() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                jobs.add(mapJob(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public static List<Job> searchJobs(String query) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE " +
                     "title LIKE ? OR company LIKE ? OR location LIKE ? OR description LIKE ?";
        String like = "%" + query + "%";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jobs.add(mapJob(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public static List<Job> getJobsByEmployer(int employerId) {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE employer_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                jobs.add(mapJob(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public static boolean deleteJob(int jobId) {
        String sql = "DELETE FROM jobs WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Job mapJob(ResultSet rs) throws SQLException {
        return new Job(
                rs.getInt("id"),
                rs.getInt("employer_id"),
                rs.getString("title"),
                rs.getString("company"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getDouble("salary"),
                rs.getString("type")
        );
    }

    // ---------- APPLICATIONS ----------

    public static boolean applyForJob(int jobId, int employeeId, String coverLetter) {
        String sql = "INSERT INTO applications(job_id, employee_id, cover_letter) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employeeId);
            ps.setString(3, coverLetter);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Application> getApplicationsForJob(int jobId) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.id, a.job_id, a.employee_id, u.full_name, u.email " +
                     "FROM applications a JOIN users u ON a.employee_id = u.id " +
                     "WHERE a.job_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Application(
                        rs.getInt("id"),
                        rs.getInt("job_id"),
                        rs.getInt("employee_id"),
                        rs.getString("full_name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean hasAlreadyApplied(int jobId, int employeeId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE job_id = ? AND employee_id = ?";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employeeId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
}

}
