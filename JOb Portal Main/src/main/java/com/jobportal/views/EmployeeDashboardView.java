package com.jobportal.views;

import com.jobportal.Database;
import com.jobportal.Main;
import com.jobportal.Session;
import com.jobportal.models.Activity;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDashboardView {

    private BorderPane root;
    private Sidebar sidebar;

    private Label welcomeLabel;

    // jobs
    private TableView<Job> jobsTable;
    private TextField searchField;
    private TextArea jobDetailsArea;
    private TextArea coverLetterArea;

    // applications
    private TableView<Application> applicationsTable;

    // profile
    private TextField nameField;
    private DatePicker birthdatePicker;
    private TextArea descriptionArea;
    private TextArea experienceArea;
    private TextArea educationArea;
    private TextArea skillsArea;

    // activity
    private TextArea activityInput;
    private ListView<String> activityList;

    private ObservableList<Job> jobsData = FXCollections.observableArrayList();
    private ObservableList<Application> applicationsData = FXCollections.observableArrayList();

    public EmployeeDashboardView() {
        createUI();
        loadUserData();
        loadJobs();
        loadApplications();
        loadActivities();
        showHome();
    }

    private void createUI() {
        root = new BorderPane();
        root.setStyle(Theme.APP_BACKGROUND);

        User user = Session.getCurrentUser();
        String name = user != null ? user.getFullName() : "Employee";

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

        // create views
        VBox homeView = createHomeView();
        VBox jobsView = createJobsView();
        VBox appsView = createApplicationsView();
        VBox profileView = createProfileView();
        VBox activityView = createActivityView();

        contentStack.getChildren().addAll(homeView, jobsView, appsView, profileView, activityView);

        centerContainer.getChildren().addAll(welcomeLabel, contentStack);
        root.setCenter(centerContainer);

        // store as user data for switching
        root.setUserData(new VBox[]{homeView, jobsView, appsView, profileView, activityView});
    }

    private VBox createHomeView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);
        Label title = new Label("Dashboard Overview");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label text = new Label(
                "\u2022 Browse jobs tailored to you.\n" +
                "\u2022 Apply quickly with a single click.\n" +
                "\u2022 Keep your profile updated so employers can understand your skills.\n" +
                "\u2022 Share what you're working on using Activity Feed."
        );
        text.setTextFill(Color.web("#9ca3af"));

        box.getChildren().addAll(title, text);
        return box;
    }

    private VBox createJobsView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);

        HBox topBar = new HBox(8);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Jobs");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("Search by title, company or location...");
        searchField.setStyle(Theme.TEXTFIELD_DARK);
        searchField.setPrefWidth(260);
        searchField.textProperty().addListener((obs, o, n) -> filterJobs());

        topBar.getChildren().addAll(title, spacer, searchField);

        jobsTable = new TableView<>();
        jobsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        jobsTable.setPrefHeight(260);

        TableColumn<Job, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTitle()));

        TableColumn<Job, String> colCompany = new TableColumn<>("Company");
        colCompany.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCompany()));

        TableColumn<Job, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));

        TableColumn<Job, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        jobsTable.getColumns().addAll(colTitle, colCompany, colLocation, colType);
        jobsTable.setItems(jobsData);

        jobDetailsArea = new TextArea();
        jobDetailsArea.setEditable(false);
        jobDetailsArea.setWrapText(true);
        jobDetailsArea.setPrefRowCount(4);
        jobDetailsArea.setStyle(Theme.TEXTAREA_DARK);

        jobsTable.getSelectionModel().selectedItemProperty().addListener((obs, old, job) -> {
            if (job != null) {
                jobDetailsArea.setText(
                        job.getTitle() + " at " + job.getCompany() + "\n" +
                        job.getLocation() + " • " + job.getType() + "\n" +
                        "Salary: " + job.getSalary() + "\n\n" +
                        job.getDescription()
                );
            } else {
                jobDetailsArea.clear();
            }
        });

        HBox bottomRow = new HBox(10);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        coverLetterArea = new TextArea();
        coverLetterArea.setPromptText("Optional: write a short note to the employer...");
        coverLetterArea.setWrapText(true);
        coverLetterArea.setStyle(Theme.TEXTAREA_DARK);
        coverLetterArea.setPrefRowCount(3);

        Button applyButton = new Button("Apply");
        applyButton.setStyle(Theme.PRIMARY_BUTTON);
        applyButton.setOnAction(e -> handleApply());

        bottomRow.getChildren().addAll(coverLetterArea, applyButton);
        HBox.setHgrow(coverLetterArea, Priority.ALWAYS);

        box.getChildren().addAll(topBar, jobsTable, jobDetailsArea, bottomRow);
        return box;
    }

    private VBox createApplicationsView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);

        Label title = new Label("My Applications");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        applicationsTable = new TableView<>();
        applicationsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        applicationsTable.setItems(applicationsData);

        TableColumn<Application, String> colJob = new TableColumn<>("Job");
        colJob.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getJobTitle()));

        TableColumn<Application, String> colCompany = new TableColumn<>("Company");
        colCompany.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCompany()));

        TableColumn<Application, String> colLocation = new TableColumn<>("Location");
        colLocation.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocation()));

        TableColumn<Application, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType()));

        applicationsTable.getColumns().addAll(colJob, colCompany, colLocation, colType);

        box.getChildren().addAll(title, applicationsTable);
        return box;
    }

    private VBox createProfileView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);

        Label title = new Label("Profile");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);

        nameField = new TextField();
        nameField.setStyle("""
        -fx-text-fill: #100036ff;
        -fx-prompt-text-fill: #888888;
        -fx-background-color: #2b2b2b;
    """);;

        birthdatePicker = new DatePicker();

        descriptionArea = new TextArea();
        descriptionArea.setStyle("""
        -fx-text-fill: #100036ff;
        -fx-prompt-text-fill: #888888;
        -fx-background-color: #2b2b2b;
    """);
        descriptionArea.setPromptText("Short bio");

        experienceArea = new TextArea();
        experienceArea.setStyle("""
        -fx-text-fill: #100036ff;
        -fx-prompt-text-fill: #888888;
        -fx-background-color: #2b2b2b;
    """);
        experienceArea.setPromptText("Experience");

        educationArea = new TextArea();
        educationArea.setStyle("""
        -fx-text-fill: #100036ff;
        -fx-prompt-text-fill: #888888;
        -fx-background-color: #2b2b2b;
    """);
        educationArea.setPromptText("Education");

        skillsArea = new TextArea();
        skillsArea.setStyle("""
        -fx-text-fill: #100036ff;
        -fx-prompt-text-fill: #888888;
        -fx-background-color: #2b2b2b;
    """);
        skillsArea.setPromptText("Skills");

        int row = 0;
        grid.add(label("Full Name"), 0, row);
        grid.add(nameField, 1, row++);
        grid.add(label("Birthdate"), 0, row);
        grid.add(birthdatePicker, 1, row++);
        grid.add(label("Description"), 0, row);
        grid.add(descriptionArea, 1, row++);
        grid.add(label("Experience"), 0, row);
        grid.add(experienceArea, 1, row++);
        grid.add(label("Education"), 0, row);
        grid.add(educationArea, 1, row++);
        grid.add(label("Skills"), 0, row);
        grid.add(skillsArea, 1, row++);

        Button saveButton = new Button("Save Profile");
        saveButton.setStyle(Theme.PRIMARY_BUTTON);
        saveButton.setOnAction(e -> handleSaveProfile());

        box.getChildren().addAll(title, grid, saveButton);
        VBox.setVgrow(grid, Priority.ALWAYS);

        return box;
    }

    private VBox createActivityView() {
        VBox box = new VBox(10);
        box.setStyle(Theme.CARD_DARK);

        Label title = new Label("Activity Feed");
        title.setTextFill(Color.WHITE);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        activityInput = new TextArea();
        activityInput.setPromptText("Share what you're working on...");
        activityInput.setWrapText(true);
        activityInput.setStyle(Theme.TEXTAREA_DARK);
        activityInput.setPrefRowCount(2);

        Button postButton = new Button("Post");
        postButton.setStyle(Theme.PRIMARY_BUTTON);
        postButton.setOnAction(e -> handlePostActivity());

        HBox postRow = new HBox(8, activityInput, postButton);
        HBox.setHgrow(activityInput, Priority.ALWAYS);

        activityList = new ListView<>();
        activityList.setStyle("-fx-background-color: transparent; -fx-control-inner-background: #020617;");

        box.getChildren().addAll(title, postRow, activityList);
        VBox.setVgrow(activityList, Priority.ALWAYS);
        return box;
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.web("#9ca3af"));
        return l;
    }

    private void switchView(int index) {
        VBox[] views = (VBox[]) root.getUserData();
        for (int i = 0; i < views.length; i++) {
            views[i].setVisible(i == index);
            views[i].setManaged(i == index);
        }
    }

    private void showHome() {
        welcomeLabel.setText("Your Job Portal at a glance");
        switchView(0);
    }

    private void showJobs() {
        welcomeLabel.setText("Browse Jobs");
        switchView(1);
    }

    private void showApplications() {
        welcomeLabel.setText("Your Applications");
        switchView(2);
    }

    private void showProfile() {
        welcomeLabel.setText("Your Profile");
        switchView(3);
    }

    private void showActivity() {
        welcomeLabel.setText("Activity Feed");
        switchView(4);
    }

    private void loadUserData() {
        User user = Session.getCurrentUser();
        if (user == null) return;

        nameField.setText(user.getFullName());
        LocalDate bd = user.getBirthdate();
        if (bd != null) {
            birthdatePicker.setValue(bd);
        }
        descriptionArea.setText(user.getDescription());
        experienceArea.setText(user.getExperience());
        educationArea.setText(user.getEducation());
        skillsArea.setText(user.getSkills());
    }

    private void loadJobs() {
        List<Job> jobs = Database.getAllJobs();
        jobsData.setAll(jobs);
    }

    private void filterJobs() {
        String keyword = searchField.getText();
        if (keyword == null || keyword.isBlank()) {
            loadJobs();
            return;
        }
        String lower = keyword.toLowerCase();
        List<Job> filtered = jobsData.stream()
                .filter(j ->
                        j.getTitle().toLowerCase().contains(lower) ||
                        j.getCompany().toLowerCase().contains(lower) ||
                        j.getLocation().toLowerCase().contains(lower))
                .collect(Collectors.toList());
        jobsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private void handleApply() {
        Job selected = jobsTable.getSelectionModel().getSelectedItem();
        User user = Session.getCurrentUser();
        if (selected == null || user == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a job first.");
            return;
        }
        String cover = coverLetterArea.getText() == null ? "" : coverLetterArea.getText();

        boolean success = Database.applyForJob(selected.getId(), user.getId(), cover);
        if (!success) {
            showAlert(Alert.AlertType.INFORMATION, "You have already applied to this job.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Application submitted!");
            coverLetterArea.clear();
            loadApplications();
        }
    }

    private void loadApplications() {
        User user = Session.getCurrentUser();
        if (user == null) return;
        List<Application> apps = Database.getApplicationsForEmployee(user.getId());
        applicationsData.setAll(apps);
    }

    private void handleSaveProfile() {
        User user = Session.getCurrentUser();
        if (user == null) return;

        user.setFullName(nameField.getText());
        user.setBirthdate(birthdatePicker.getValue());
        user.setDescription(descriptionArea.getText());
        user.setExperience(experienceArea.getText());
        user.setEducation(educationArea.getText());
        user.setSkills(skillsArea.getText());

        if (Database.updateUserProfile(user)) {
            showAlert(Alert.AlertType.INFORMATION, "Profile updated.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to update profile.");
        }
    }

    private void handlePostActivity() {
        User user = Session.getCurrentUser();
        if (user == null) return;
        String text = activityInput.getText();
        if (text == null || text.isBlank()) {
            return;
        }
        if (Database.addActivity(user.getId(), text)) {
            activityInput.clear();
            loadActivities();
        }
    }

    private void loadActivities() {
        List<Activity> activities = Database.getRecentActivities();
        activityList.getItems().setAll(
                activities.stream()
                        .map(a -> a.getUserName() + " • " + a.getContent())
                        .collect(Collectors.toList())
        );
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type, msg);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return root;
    }
}
