package com.jobportal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Validation error", "Please enter both email and password.");
            return;
        }

        User user = Database.login(email, password);
        if (user == null) {
            showAlert("Login failed", "Invalid email or password.");
            return;
        }

        Session.setCurrentUser(user);

        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            if (user.isEmployer()) {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/employer_dashboard.fxml"))));
                stage.setTitle("Job Portal - Employer");
            } else {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/employee_dashboard.fxml"))));
                stage.setTitle("Job Portal - Employee");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load dashboard.");
        }
    }

    @FXML
    private void openRegister() {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/register.fxml"))));
            stage.setTitle("Job Portal - Register");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open registration screen.");
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
