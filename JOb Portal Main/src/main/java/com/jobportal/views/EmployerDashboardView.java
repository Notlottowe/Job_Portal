package com.jobportal.views;

import com.jobportal.Database;
import com.jobportal.Main;
import com.jobportal.Session;
import com.jobportal.models.Application;
import com.jobportal.models.Job;
import com.jobportal.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class EmployerDashboardView {

    private BorderPane root;
    private Sidebar sidebar;

    private Label welcomeLabel;

    // jobs
    private TableView<Job> jobsTable;
    private ObservableList<Job> jobsData = FXCollections.observableArrayList();

    // job form
    private TextField titleField;
    private TextField companyField;
    private TextField locationField;
    private TextField salaryField;
    private TextField typeField;
    private TextArea descriptionArea;

    // applications
    private TableView<Application> appsTable;
    private ObservableList<Application> appsData = FXCollections.observableArrayList();
    private TextArea applicantDetailsArea;

    public EmployerDashboardView() {
        createUI();
        loadJobs();
        showHome();
    }

    private void createUI() {
        root = new BorderPane();
        root.setStyle(Theme.APP_BACKGROUND);

        User user = Session.getCurrentUser();
        String name = user != null ? user.getFullName() : "Employer";

        sidebar = new Sidebar("Hi, " + name,
                section -> {
                    sidebar.setActive(section);
                    switch (section) {
                        case HOME -> showHome();
                        case JOBS -> showJobs();
                        case APPLICATIONS -> showApplications();
                        case PROFILE -> showProfile();
                        case ACTIVITY -> showActivity();
                    }
                },
                () -> {
                    Session.clear();
                    Main.showLogin();
                });

        root.setLeft(sidebar.getRoot());

        VBox centerContainer = new VBox();
        centerContainer.setPadding(new Insets(18));
        centerContainer.setSpacing(16);

        welcomeLabel = new Label();
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        StackPane contentStack = new StackPane();
        contentStack.setPadding(new Insets(10, 0, 0, 0));

        VBox homeView = createHomeView();
        VBox jobsView = createJobsView();
        VBox appsView = createApplicationsView();
        VBox profileView = createProfileView();
        VBox activityView = createActivityView();

        contentStack.getChildren().addAll(homeView, jobsView, appsView, profileView, activityView);

        centerContainer.getChildren().addAll(welcomeLabel, contentStack);
        root.setCenter(centerContainer);

        root.setUserData(new VBox[]{homeView, jobsView, appsView, profileView, activityView});
    }

    private VBox createHomeView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);
        Label title = new Label("Employer Overview");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label text = new Label(
                "Post jobs, manage candidates and keep your company presence active.\n" +
                "You can review who applied and view details of each applicant."
        );
        text.setTextFill(Color.web("#9ca3af"));

        box.getChildren().addAll(title, text);
        return box;
    }

    private VBox createJobsView() {
        VBox container = new VBox(10);
        container.setStyle(Theme.CARD_DARK);

        Label title = new Label("Job Management");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox mainRow = new HBox(16);

        // left: job list
        VBox listBox = new VBox(8);
        Label listLabel = new Label("Your Jobs");
        listLabel.setTextFill(Color.web("#9ca3af"));

        jobsTable = new TableView<>();
        jobsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jobsTable.setItems(jobsData);
        jobsTable.setPrefHeight(260);

        TableColumn<Job, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));
        TableColumn<Job, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));
        TableColumn<Job, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        jobsTable.getColumns().addAll(colTitle, colLocation, colType);

        HBox jobButtons = new HBox(8);
        jobButtons.setAlignment(Pos.CENTER_LEFT);

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(Theme.SECONDARY_BUTTON);
        refreshButton.setOnAction(e -> loadJobs());

        Button deleteButton = new Button("Delete Selected");
        deleteButton.setStyle(Theme.DANGER_BUTTON);
        deleteButton.setOnAction(e -> handleDeleteJob());

        Button viewAppsButton = new Button("View Applicants");
        viewAppsButton.setStyle(Theme.PRIMARY_BUTTON);
        viewAppsButton.setOnAction(e -> loadApplicationsForSelectedJob());

        jobButtons.getChildren().addAll(refreshButton, viewAppsButton, deleteButton);

        listBox.getChildren().addAll(listLabel, jobsTable, jobButtons);
        VBox.setVgrow(jobsTable, Priority.ALWAYS);

        // right: job form
        VBox formBox = new VBox(8);
        formBox.setPrefWidth(360);
        Label formLabel = new Label("Post New Job");
        formLabel.setTextFill(Color.web("#9ca3af"));

        titleField = new TextField();
        titleField.setPromptText("Job Title");
        titleField.setStyle(Theme.TEXTFIELD_DARK);

        companyField = new TextField();
        companyField.setPromptText("Company Name");
        companyField.setStyle(Theme.TEXTFIELD_DARK);

        locationField = new TextField();
        locationField.setPromptText("Location");
        locationField.setStyle(Theme.TEXTFIELD_DARK);

        salaryField = new TextField();
        salaryField.setPromptText("Salary (optional)");
        salaryField.setStyle(Theme.TEXTFIELD_DARK);

        typeField = new TextField();
        typeField.setPromptText("Job Type (Full-time, Part-time, Remote...)");
        typeField.setStyle(Theme.TEXTFIELD_DARK);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Describe the role, responsibilities and expectations...");
        descriptionArea.setWrapText(true);
        descriptionArea.setStyle(Theme.TEXTAREA_DARK);
        descriptionArea.setPrefRowCount(5);

        Button postButton = new Button("Post Job");
        postButton.setStyle(Theme.PRIMARY_BUTTON);
        postButton.setOnAction(e -> handlePostJob());

        formBox.getChildren().addAll(formLabel, titleField, companyField, locationField,
                salaryField, typeField, descriptionArea, postButton);

        mainRow.getChildren().addAll(listBox, formBox);
        HBox.setHgrow(listBox, Priority.ALWAYS);

        container.getChildren().addAll(title, mainRow);
        return container;
    }

    private VBox createApplicationsView() {
        VBox container = new VBox(10);
        container.setStyle(Theme.CARD_DARK);

        Label title = new Label("Applicants");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        appsTable = new TableView<>();
        appsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        appsTable.setItems(appsData);

        TableColumn<Application, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getJobTitle()));

        TableColumn<Application, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCompany()));

        appsTable.getColumns().addAll(colName, colEmail);

        applicantDetailsArea = new TextArea();
        applicantDetailsArea.setEditable(false);
        applicantDetailsArea.setWrapText(true);
        applicantDetailsArea.setStyle(Theme.TEXTAREA_DARK);
        applicantDetailsArea.setPrefRowCount(6);

        appsTable.getSelectionModel().selectedItemProperty().addListener((obs, old, app) -> {
            if (app == null) {
                applicantDetailsArea.clear();
            } else {
                User u = Database.getUserById(app.getEmployeeId());
                if (u != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Name: ").append(u.getFullName()).append("\n");
                    sb.append("Email: ").append(u.getEmail()).append("\n\n");
                    sb.append("Description: ").append(nullSafe(u.getDescription())).append("\n\n");
                    sb.append("Experience: ").append(nullSafe(u.getExperience())).append("\n\n");
                    sb.append("Education: ").append(nullSafe(u.getEducation())).append("\n\n");
                    sb.append("Skills: ").append(nullSafe(u.getSkills())).append("\n\n");
                    sb.append("Cover Letter:\n").append(nullSafe(app.getCoverLetter()));
                    applicantDetailsArea.setText(sb.toString());
                } else {
                    applicantDetailsArea.setText("Unable to load applicant details.");
                }
            }
        });

        container.getChildren().addAll(title, appsTable, applicantDetailsArea);
        VBox.setVgrow(appsTable, Priority.ALWAYS);
        return container;
    }

    private VBox createProfileView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);

        Label title = new Label("Company Profile");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label text = new Label("For simplicity, this demo focuses on job and applicant management.\n" +
                "You can extend this section with real company profile editing.");
        text.setTextFill(Color.web("#9ca3af"));

        box.getChildren().addAll(title, text);
        return box;
    }

    private VBox createActivityView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);

        Label title = new Label("Employer Tips");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label text = new Label(
                "• Write clear job descriptions.\n" +
                "• Highlight culture and benefits.\n" +
                "• Review applications regularly.\n" +
                "• Keep candidates informed."
        );
        text.setTextFill(Color.web("#9ca3af"));

        box.getChildren().addAll(title, text);
        return box;
    }

    private void switchView(int index) {
        VBox[] views = (VBox[]) root.getUserData();
        for (int i = 0; i < views.length; i++) {
            views[i].setVisible(i == index);
            views[i].setManaged(i == index);
        }
    }

    private void showHome() {
        welcomeLabel.setText("Manage your hiring pipeline");
        switchView(0);
    }

    private void showJobs() {
        welcomeLabel.setText("Jobs and postings");
        switchView(1);
    }

    private void showApplications() {
        welcomeLabel.setText("Applicants");
        switchView(2);
    }

    private void showProfile() {
        welcomeLabel.setText("Company profile");
        switchView(3);
    }

    private void showActivity() {
        welcomeLabel.setText("Hiring tips");
        switchView(4);
    }

    private void loadJobs() {
        User user = Session.getCurrentUser();
        if (user == null) return;
        List<Job> jobs = Database.getJobsForEmployer(user.getId());
        jobsData.setAll(jobs);
    }

    private void handlePostJob() {
        User user = Session.getCurrentUser();
        if (user == null) return;

        String title = titleField.getText();
        String company = companyField.getText();
        String location = locationField.getText();
        String salaryText = salaryField.getText();
        String type = typeField.getText();
        String desc = descriptionArea.getText();

        if (title.isBlank() || company.isBlank() || location.isBlank() || type.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in the required fields.");
            return;
        }

        double salary = 0;
        if (!salaryText.isBlank()) {
            try {
                salary = Double.parseDouble(salaryText);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Invalid salary value.");
                return;
            }
        }

        Job job = new Job(user.getId(), title, company, location, salary, type, desc);
        if (Database.addJob(job)) {
            showAlert(Alert.AlertType.INFORMATION, "Job posted successfully.");
            clearJobForm();
            loadJobs();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to post job.");
        }
    }

    private void handleDeleteJob() {
        User user = Session.getCurrentUser();
        if (user == null) return;
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a job first.");
            return;
        }
        if (Database.deleteJob(selected.getId(), user.getId())) {
            showAlert(Alert.AlertType.INFORMATION, "Job deleted.");
            loadJobs();
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to delete job.");
        }
    }

    private void loadApplicationsForSelectedJob() {
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Select a job first.");
            return;
        }
        List<Application> apps = Database.getApplicationsForJob(selected.getId());
        appsData.setAll(apps);
        showApplications();
    }

    private void clearJobForm() {
        titleField.clear();
        companyField.clear();
        locationField.clear();
        salaryField.clear();
        typeField.clear();
        descriptionArea.clear();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    public Parent getRoot() {
        return root;
    }
}
