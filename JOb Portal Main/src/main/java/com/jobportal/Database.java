package com.jobportal;

import com.jobportal.models.Activity;
import com.jobportal.models.Application;
import com.jobportal.models.Job;
import com.jobportal.models.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/job_portal_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; // TODO: set your MySQL user
    private static final String DB_PASSWORD = "MeghP169"; // TODO: set your MySQL password

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

    // region User

    public static boolean registerUser(User user) {
        String sql = "INSERT INTO users(full_name, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in registerUser: " + e.getMessage());
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
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                Date bd = rs.getDate("birthdate");
                if (bd != null) {
                    u.setBirthdate(bd.toLocalDate());
                }
                u.setDescription(rs.getString("description"));
                u.setExperience(rs.getString("experience"));
                u.setEducation(rs.getString("education"));
                u.setSkills(rs.getString("skills"));
                return u;
            }
        } catch (SQLException e) {
            System.err.println("Error in login: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateUserProfile(User user) {
        String sql = "UPDATE users SET full_name=?, birthdate=?, description=?, experience=?, education=?, skills=? WHERE id=?";
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
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in updateUserProfile: " + e.getMessage());
            return false;
        }
    }

    public static User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                Date bd = rs.getDate("birthdate");
                if (bd != null) u.setBirthdate(bd.toLocalDate());
                u.setDescription(rs.getString("description"));
                u.setExperience(rs.getString("experience"));
                u.setEducation(rs.getString("education"));
                u.setSkills(rs.getString("skills"));
                return u;
            }
        } catch (SQLException e) {
            System.err.println("Error in getUserById: " + e.getMessage());
        }
        return null;
    }

    // endregion

    // region Jobs

    public static boolean addJob(Job job) {
        String sql = "INSERT INTO jobs(employer_id, title, company, location, salary, type, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, job.getEmployerId());
            ps.setString(2, job.getTitle());
            ps.setString(3, job.getCompany());
            ps.setString(4, job.getLocation());
            ps.setDouble(5, job.getSalary());
            ps.setString(6, job.getType());
            ps.setString(7, job.getDescription());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in addJob: " + e.getMessage());
            return false;
        }
    }

    public static List<Job> getJobsForEmployer(int employerId) {
        List<Job> list = new ArrayList<>();
        String sql = "SELECT * FROM jobs WHERE employer_id = ? ORDER BY id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Job j = mapJob(rs);
                list.add(j);
            }
        } catch (SQLException e) {
            System.err.println("Error in getJobsForEmployer: " + e.getMessage());
        }
        return list;
    }

    public static List<Job> getAllJobs() {
        List<Job> list = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Job j = mapJob(rs);
                list.add(j);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllJobs: " + e.getMessage());
        }
        return list;
    }

    private static Job mapJob(ResultSet rs) throws SQLException {
        Job j = new Job();
        j.setId(rs.getInt("id"));
        j.setEmployerId(rs.getInt("employer_id"));
        j.setTitle(rs.getString("title"));
        j.setCompany(rs.getString("company"));
        j.setLocation(rs.getString("location"));
        j.setSalary(rs.getDouble("salary"));
        j.setType(rs.getString("type"));
        j.setDescription(rs.getString("description"));
        return j;
    }

    public static boolean deleteJob(int jobId, int employerId) {
        String sql = "DELETE FROM jobs WHERE id = ? AND employer_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employerId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteJob: " + e.getMessage());
            return false;
        }
    }

    // endregion

    // region Applications

    public static boolean hasAlreadyApplied(int jobId, int employeeId) {
        String sql = "SELECT id FROM applications WHERE job_id = ? AND employee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employeeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error in hasAlreadyApplied: " + e.getMessage());
            return false;
        }
    }

    public static boolean applyForJob(int jobId, int employeeId, String coverLetter) {
        if (hasAlreadyApplied(jobId, employeeId)) {
            return false;
        }
        String sql = "INSERT INTO applications(job_id, employee_id, cover_letter) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, employeeId);
            ps.setString(3, coverLetter);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in applyForJob: " + e.getMessage());
            return false;
        }
    }

    public static List<Application> getApplicationsForEmployee(int employeeId) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, j.title, j.company, j.location, j.type " +
                     "FROM applications a JOIN jobs j ON a.job_id = j.id " +
                     "WHERE a.employee_id = ? ORDER BY a.id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setJobId(rs.getInt("job_id"));
                app.setEmployeeId(rs.getInt("employee_id"));
                app.setCoverLetter(rs.getString("cover_letter"));
                app.setJobTitle(rs.getString("title"));
                app.setCompany(rs.getString("company"));
                app.setLocation(rs.getString("location"));
                app.setType(rs.getString("type"));
                list.add(app);
            }
        } catch (SQLException e) {
            System.err.println("Error in getApplicationsForEmployee: " + e.getMessage());
        }
        return list;
    }

    public static List<Application> getApplicationsForJob(int jobId) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name, u.email " +
                     "FROM applications a JOIN users u ON a.employee_id = u.id " +
                     "WHERE a.job_id = ? ORDER BY a.id DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Application app = new Application();
                app.setId(rs.getInt("id"));
                app.setJobId(rs.getInt("job_id"));
                app.setEmployeeId(rs.getInt("employee_id"));
                app.setCoverLetter(rs.getString("cover_letter"));
                app.setJobTitle(rs.getString("full_name")); // reuse field for name/email if needed
                app.setCompany(rs.getString("email"));
                list.add(app);
            }
        } catch (SQLException e) {
            System.err.println("Error in getApplicationsForJob: " + e.getMessage());
        }
        return list;
    }

    // endregion

    // region Activities

    public static boolean addActivity(int userId, String content) {
        String sql = "INSERT INTO activities(user_id, content) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, content);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error in addActivity: " + e.getMessage());
            return false;
        }
    }

    public static List<Activity> getRecentActivities() {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT a.id, a.user_id, a.content, a.created_at, u.full_name " +
                     "FROM activities a JOIN users u ON a.user_id = u.id " +
                     "ORDER BY a.created_at DESC LIMIT 50";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Activity act = new Activity();
                act.setId(rs.getInt("id"));
                act.setUserId(rs.getInt("user_id"));
                act.setContent(rs.getString("content"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    act.setCreatedAt(ts.toLocalDateTime());
                } else {
                    act.setCreatedAt(LocalDateTime.now());
                }
                act.setUserName(rs.getString("full_name"));
                list.add(act);
            }
        } catch (SQLException e) {
            System.err.println("Error in getRecentActivities: " + e.getMessage());
        }
        return list;
    }

    // endregion
}
