package com.jobportal.views;

import com.jobportal.Database;
import com.jobportal.Main;
import com.jobportal.Session;
import com.jobportal.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LoginView {

    private BorderPane root;

    private TextField emailField;
    private PasswordField passwordField;
    private ComboBox<String> roleComboBox;
    private Label errorLabel;

    public LoginView() {
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        root.setStyle(Theme.APP_BACKGROUND);

        // Center glass card
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(28));
        card.setStyle(Theme.CARD_DARK);

        Label title = new Label("Welcome back");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", 24));

        Label subtitle = new Label("Sign in to Job Portal");
        subtitle.setTextFill(Color.web("#9ca3af"));

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);
        emailField.setStyle(Theme.TEXTFIELD_DARK);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(300);
        passwordField.setStyle(Theme.TEXTFIELD_DARK);

        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Employer");
        roleComboBox.setPromptText("Select Role");
        roleComboBox.setMaxWidth(300);
        roleComboBox.setStyle(Theme.TEXTFIELD_DARK);

        Button loginButton = new Button("Sign In");
        loginButton.setMaxWidth(300);
        loginButton.setStyle(Theme.PRIMARY_BUTTON);
        loginButton.setOnAction(e -> handleLogin());

        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#f97373"));

        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setTextFill(Color.web("#9ca3af"));

        Hyperlink registerLink = new Hyperlink("Create one");
        registerLink.setTextFill(Color.web("#38bdf8"));
        registerLink.setOnAction(e -> Main.showRegister());

        HBox bottomRow = new HBox(4, noAccountLabel, registerLink);
        bottomRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                title,
                subtitle,
                emailField,
                passwordField,
                roleComboBox,
                loginButton,
                errorLabel,
                bottomRow
        );

        BorderPane.setAlignment(card, Pos.CENTER);
        root.setCenter(card);
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (email.isEmpty() || password.isEmpty() || selectedRole == null) {
            errorLabel.setText("Please fill all fields and select a role.");
            return;
        }

        User user = Database.login(email, password);
        if (user == null) {
            errorLabel.setText("Invalid email or password.");
            return;
        }
        if (!user.getRole().equalsIgnoreCase(selectedRole)) {
            errorLabel.setText("Selected role does not match your account.");
            return;
        }

        Session.setCurrentUser(user);
        if (user.getRole().equalsIgnoreCase("Employer")) {
            Main.showEmployerDashboard();
        } else {
            Main.showEmployeeDashboard();
        }
    }

    public Parent getRoot() {
        return root;
    }
}
