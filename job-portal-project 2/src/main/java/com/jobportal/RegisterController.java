package com.jobportal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button registerButton;

    @FXML
    public void initialize() {
        roleComboBox.getItems().setAll("Employee", "Employer");
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        String role = roleComboBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty() || role == null) {
            showAlert("Validation error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirm)) {
            showAlert("Validation error", "Passwords do not match.");
            return;
        }

        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        boolean ok = Database.registerUser(user);
        if (!ok) {
            showAlert("Registration failed", "Email might already be in use.");
            return;
        }

        showAlert("Success", "Registration successful. Please log in.");

        backToLogin();
    }

    @FXML
    private void backToLogin() {
        try {
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/login.fxml"))));
            stage.setTitle("Job Portal - Login");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Cannot return to login screen.");
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
