package com.jobportal.views;

import com.jobportal.Database;
import com.jobportal.Main;
import com.jobportal.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RegisterView {

    private BorderPane root;

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> roleComboBox;
    private Label errorLabel;

    public RegisterView() {
        createUI();
    }

    private void createUI() {
        root = new BorderPane();
        root.setStyle(Theme.APP_BACKGROUND);

        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(28));
        card.setStyle(Theme.CARD_DARK);

        Label title = new Label("Create your account");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", 22));

        nameField = new TextField();
        nameField.setPromptText("Full Name");
        nameField.setMaxWidth(320);
        nameField.setStyle(Theme.TEXTFIELD_DARK);

        emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(320);
        emailField.setStyle(Theme.TEXTFIELD_DARK);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);
        passwordField.setStyle(Theme.TEXTFIELD_DARK);

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(320);
        confirmPasswordField.setStyle(Theme.TEXTFIELD_DARK);

        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Employee", "Employer");
        roleComboBox.setPromptText("Select Role");
        roleComboBox.setMaxWidth(320);
        roleComboBox.setStyle(Theme.TEXTFIELD_DARK);

        Button registerButton = new Button("Sign Up");
        registerButton.setMaxWidth(320);
        registerButton.setStyle(Theme.PRIMARY_BUTTON);
        registerButton.setOnAction(e -> handleRegister());

        errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#f97373"));

        Hyperlink backToLogin = new Hyperlink("Back to login");
        backToLogin.setTextFill(Color.web("#38bdf8"));
        backToLogin.setOnAction(e -> Main.showLogin());

        card.getChildren().addAll(
                title,
                nameField,
                emailField,
                passwordField,
                confirmPasswordField,
                roleComboBox,
                registerButton,
                errorLabel,
                backToLogin
        );

        BorderPane.setAlignment(card, Pos.CENTER);
        root.setCenter(card);
    }

    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty() || role == null) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }
        if (!password.equals(confirm)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        User user = new User(name, email, password, role);
        if (Database.registerUser(user)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration successful! Please login.");
            alert.showAndWait();
            Main.showLogin();
        } else {
            errorLabel.setText("Registration failed. Email might already be in use.");
        }
    }

    public Parent getRoot() {
        return root;
    }
}
