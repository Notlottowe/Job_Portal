package com.jobportal.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.function.Consumer;

public class Sidebar {

    private VBox root;
    private Button homeButton;
    private Button jobsButton;
    private Button appsButton;
    private Button profileButton;
    private Button activityButton;
    private Button logoutButton;

    public enum Section {
        HOME, JOBS, APPLICATIONS, PROFILE, ACTIVITY
    }

    public Sidebar(String headerText, Consumer<Section> onSectionSelected, Runnable onLogout) {
        root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setPrefWidth(220);
        root.setStyle(Theme.SIDEBAR);

        Label title = new Label(headerText);
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", 20));
        title.setPadding(new Insets(0, 0, 10, 2));

        homeButton = createSidebarButton("Overview", () -> onSectionSelected.accept(Section.HOME));
        jobsButton = createSidebarButton("Jobs", () -> onSectionSelected.accept(Section.JOBS));
        appsButton = createSidebarButton("My Applications", () -> onSectionSelected.accept(Section.APPLICATIONS));
        profileButton = createSidebarButton("Profile", () -> onSectionSelected.accept(Section.PROFILE));
        activityButton = createSidebarButton("Activity Feed", () -> onSectionSelected.accept(Section.ACTIVITY));
        logoutButton = new Button("Logout");
        logoutButton.setStyle(Theme.DANGER_BUTTON);
        logoutButton.setMaxWidth(Double.MAX_VALUE);
        logoutButton.setOnAction(e -> onLogout.run());

        VBox top = new VBox(8, title, homeButton, jobsButton, appsButton, profileButton, activityButton);
        VBox.setMargin(top, new Insets(0, 0, 40, 0));

        root.getChildren().addAll(top, logoutButton);
        root.setAlignment(Pos.TOP_LEFT);

        setActive(Section.HOME);
    }

    private Button createSidebarButton(String text, Runnable onClick) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(Theme.SIDEBAR_BUTTON);
        btn.setOnAction(e -> onClick.run());
        return btn;
    }

    public void setActive(Section section) {
        homeButton.setStyle(Theme.SIDEBAR_BUTTON);
        jobsButton.setStyle(Theme.SIDEBAR_BUTTON);
        appsButton.setStyle(Theme.SIDEBAR_BUTTON);
        profileButton.setStyle(Theme.SIDEBAR_BUTTON);
        activityButton.setStyle(Theme.SIDEBAR_BUTTON);

        switch (section) {
            case HOME -> homeButton.setStyle(Theme.SIDEBAR_BUTTON_ACTIVE);
            case JOBS -> jobsButton.setStyle(Theme.SIDEBAR_BUTTON_ACTIVE);
            case APPLICATIONS -> appsButton.setStyle(Theme.SIDEBAR_BUTTON_ACTIVE);
            case PROFILE -> profileButton.setStyle(Theme.SIDEBAR_BUTTON_ACTIVE);
            case ACTIVITY -> activityButton.setStyle(Theme.SIDEBAR_BUTTON_ACTIVE);
        }
    }

    public Node getRoot() {
        return root;
    }
}
