package com.jobportal;

import com.jobportal.views.EmployeeDashboardView;
import com.jobportal.views.EmployerDashboardView;
import com.jobportal.views.LoginView;
import com.jobportal.views.RegisterView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Job Portal - Modern");
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        showLogin();
        primaryStage.show();
    }

    public static void showLogin() {
        LoginView view = new LoginView();
        Scene scene = new Scene(view.getRoot(), 1000, 650);
        primaryStage.setScene(scene);
    }

    public static void showRegister() {
        RegisterView view = new RegisterView();
        Scene scene = new Scene(view.getRoot(), 1000, 650);
        primaryStage.setScene(scene);
    }

    public static void showEmployeeDashboard() {
        EmployeeDashboardView view = new EmployeeDashboardView();
        Scene scene = new Scene(view.getRoot(), 1200, 700);
        primaryStage.setScene(scene);
    }

    public static void showEmployerDashboard() {
        EmployerDashboardView view = new EmployerDashboardView();
        Scene scene = new Scene(view.getRoot(), 1200, 700);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
