package com.jobportal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EmployerDashboardController {

    @FXML private Label welcomeLabel;

    @FXML private TableView<Job> jobsTable;
    @FXML private TableColumn<Job, String> titleColumn;
    @FXML private TableColumn<Job, String> companyColumn;
    @FXML private TableColumn<Job, String> locationColumn;
    @FXML private TableColumn<Job, String> typeColumn;

    @FXML private TableView<Application> applicationsTable;
    @FXML private TableColumn<Application, String> applicantNameColumn;
    @FXML private TableColumn<Application, String> applicantEmailColumn;
    @FXML private TextArea applicantDetailsArea;

    @FXML private TextField titleField;
    @FXML private TextField companyField;
    @FXML private TextField locationField;
    @FXML private TextField salaryField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextArea descriptionField;

    private ObservableList<Job> jobsList = FXCollections.observableArrayList();
    private ObservableList<Application> applicationsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        User user = Session.getCurrentUser();
        if (user == null || !user.isEmployer()) {
            showAlert("Error", "No employer logged in."); // Fallback
            return;
        }

        welcomeLabel.setText("Welcome, " + user.getFullName() + " (Employer)");

        titleColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        companyColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCompany()));
        locationColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));
        typeColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        applicantNameColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmployeeName()));
        applicantEmailColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmployeeEmail()));

        typeComboBox.getItems().setAll("Full-time", "Part-time", "Remote", "Contract", "Internship");

        loadJobs();

        applicationsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldApp, newApp) -> showApplicantDetails(newApp)
        );
    }

    private void loadJobs() {
        User user = Session.getCurrentUser();
        if (user == null) return;
        List<Job> jobs = Database.getJobsByEmployer(user.getId());
        jobsList.setAll(jobs);
        jobsTable.setItems(jobsList);
    }

    @FXML
    private void handlePostJob() {
        User user = Session.getCurrentUser();
        if (user == null || !user.isEmployer()) {
            showAlert("Error", "You must be logged in as an employer.");
            return;
        }

        String title = titleField.getText().trim();
        String company = companyField.getText().trim();
        String location = locationField.getText().trim();
        String salaryText = salaryField.getText().trim();
        String type = typeComboBox.getValue();
        String description = descriptionField.getText().trim();

        if (title.isEmpty() || company.isEmpty() || location.isEmpty() ||
                salaryText.isEmpty() || type == null || description.isEmpty()) {
            showAlert("Validation error", "Please fill in all job fields.");
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            showAlert("Validation error", "Salary must be numeric.");
            return;
        }

        Job job = new Job(user.getId(), title, company, description, location, salary, type);
        if (Database.addJob(job)) {
            showAlert("Success", "Job posted.");
            clearJobForm();
            loadJobs();
        } else {
            showAlert("Error", "Failed to post job.");
        }
    }

    @FXML
    private void handleDeleteJob() {
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a job to delete.");
            return;
        }
        if (Database.deleteJob(selected.getId())) {
            showAlert("Deleted", "Job deleted.");
            loadJobs();
            applicationsList.clear();
            applicationsTable.setItems(applicationsList);
            applicantDetailsArea.clear();
        } else {
            showAlert("Error", "Failed to delete job.");
        }
    }

    @FXML
    private void handleViewApplicants() {
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a job first.");
            return;
        }
        List<Application> apps = Database.getApplicationsForJob(selected.getId());
        applicationsList.setAll(apps);
        applicationsTable.setItems(applicationsList);
        applicantDetailsArea.clear();
    }

    private void showApplicantDetails(Application app) {
        if (app == null) {
            applicantDetailsArea.clear();
            return;
        }
        User u = Database.getUserById(app.getEmployeeId());
        if (u == null) {
            applicantDetailsArea.setText("Unable to load applicant details.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(u.getFullName()).append("\n");
        sb.append("Email: ").append(u.getEmail()).append("\n");
        if (u.getBirthdate() != null) {
            sb.append("Birthdate: ").append(u.getBirthdate()).append("\n");
        }
        sb.append("\nDescription:\n").append(nz(u.getDescription())).append("\n\n");
        sb.append("Experience:\n").append(nz(u.getExperience())).append("\n\n");
        sb.append("Education:\n").append(nz(u.getEducation())).append("\n\n");
        sb.append("Skills:\n").append(nz(u.getSkills()));

        applicantDetailsArea.setText(sb.toString());
    }

    private String nz(String s) {
        return s == null ? "(none)" : s;
    }

    private void clearJobForm() {
        titleField.clear();
        companyField.clear();
        locationField.clear();
        salaryField.clear();
        descriptionField.clear();
        typeComboBox.getSelectionModel().clearSelection();
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
