package com.jobportal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDashboardController {

    @FXML private Label welcomeLabel;

    // Jobs
    @FXML private TextField searchField;
    @FXML private TableView<Job> jobsTable;
    @FXML private TableColumn<Job, String> titleColumn;
    @FXML private TableColumn<Job, String> companyColumn;
    @FXML private TableColumn<Job, String> locationColumn;
    @FXML private TableColumn<Job, String> typeColumn;
    @FXML private TextArea jobDetailsArea;

    // Profile
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private DatePicker birthdatePicker;
    @FXML private TextArea descriptionField;
    @FXML private TextArea experienceField;
    @FXML private TextArea educationField;
    @FXML private TextArea skillsField;

    private ObservableList<Job> jobsList = FXCollections.observableArrayList();
    private User user;

    @FXML
    public void initialize() {
        user = Session.getCurrentUser();
        if (user == null || !user.isEmployee()) {
            showAlert("Error", "No employee logged in.");
            return;
        }

        welcomeLabel.setText("Welcome, " + user.getFullName() + " (Employee)");

        titleColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        companyColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCompany()));
        locationColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));
        typeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        loadJobs();

        jobsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldJob, newJob) -> showJobDetails(newJob)
        );

        loadProfile();
    }

    private void loadJobs() {
        List<Job> jobs = Database.getAllJobs();
        jobsList.setAll(jobs);
        jobsTable.setItems(jobsList);
    }

    @FXML
    private void handleSearch() {
        String q = searchField.getText() == null ? "" : searchField.getText().trim();
        if (q.isEmpty()) {
            loadJobs();
        } else {
            jobsList.setAll(Database.searchJobs(q));
            jobsTable.setItems(jobsList);
        }
    }

    private void showJobDetails(Job job) {
        if (job == null) {
            jobDetailsArea.clear();
            return;
        }
        User employer = Database.getUserById(job.getEmployerId());

        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(job.getTitle()).append("\n");
        sb.append("Company: ").append(job.getCompany()).append("\n");
        sb.append("Location: ").append(job.getLocation()).append("\n");
        sb.append("Salary: ").append(job.getSalary()).append("\n");
        sb.append("Type: ").append(job.getType()).append("\n");
        if (employer != null) {
            sb.append("Posted by: ").append(employer.getFullName())
              .append(" (").append(employer.getEmail()).append(")\n");
        }
        sb.append("\nDescription:\n").append(job.getDescription());

        jobDetailsArea.setText(sb.toString());
    }

    @FXML
    private void handleApply() {
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a job to apply.");
            return;
        }

        // ✅ New: prevent duplicate application
        if (Database.hasAlreadyApplied(selected.getId(), user.getId())) {
            showAlert("Info", "You have already applied to this job.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Apply for Job");
        dialog.setHeaderText("Cover letter (optional)");
        dialog.setContentText("Enter a short message:");
        dialog.showAndWait();

        String coverLetter = dialog.getResult();
        if (Database.applyForJob(selected.getId(), user.getId(), coverLetter)) {
            showAlert("Success", "Application submitted.");
        } else {
            showAlert("Error", "Failed to submit application.");
        }
    }

    

    // -------- Profile --------

    private void loadProfile() {
        // Refresh from DB in case updated
        user = Database.getUserById(user.getId());
        if (user == null) {
            showAlert("Error", "Unable to load profile.");
            return;
        }
        nameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        birthdatePicker.setValue(user.getBirthdate());
        descriptionField.setText(nz(user.getDescription()));
        experienceField.setText(nz(user.getExperience()));
        educationField.setText(nz(user.getEducation()));
        skillsField.setText(nz(user.getSkills()));
    }

    @FXML
    private void handleSaveProfile() {
        user.setFullName(nameField.getText().trim());
        LocalDate bd = birthdatePicker.getValue();
        user.setBirthdate(bd);
        user.setDescription(descriptionField.getText().trim());
        user.setExperience(experienceField.getText().trim());
        user.setEducation(educationField.getText().trim());
        user.setSkills(skillsField.getText().trim());

        if (Database.updateUserProfile(user)) {
            showAlert("Saved", "Profile updated.");
        } else {
            showAlert("Error", "Failed to update profile.");
        }
    }

    @FXML
    private void handleResetProfile() {
        loadProfile();
    }

    private String nz(String s) {
        return s == null ? "" : s;
    }

    @FXML
    private void handleLogout() {
        Session.clear();
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/login.fxml"))));
            stage.setTitle("Job Portal - Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to return to login.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
