package com.jobportal.views;

public class Theme {

    public static final String APP_BACKGROUND =
            "-fx-background-color: linear-gradient(to bottom, #020617, #020617 40%, #020617, #020617);";

    public static final String CARD_DARK =
            "-fx-background-color: rgba(15,23,42,0.95);"
            + "-fx-background-radius: 16;"
            + "-fx-padding: 24;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 25, 0.3, 0, 8);";

    public static final String CARD_LIGHT =
            "-fx-background-color: rgba(15,23,42,0.9);"
            + "-fx-background-radius: 14;"
            + "-fx-padding: 18;"
            + "-fx-effect: dropshadow(gaussian, rgba(15,23,42,0.8), 18, 0.3, 0, 6);";

    public static final String PRIMARY_BUTTON =
            "-fx-background-color: linear-gradient(to right, #2563eb, #38bdf8);"
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-background-radius: 999;"
            + "-fx-padding: 8 18;";

    public static final String SECONDARY_BUTTON =
            "-fx-background-color: #111827;"
            + "-fx-text-fill: #e5e7eb;"
            + "-fx-background-radius: 999;"
            + "-fx-border-color: #1f2937;"
            + "-fx-border-radius: 999;"
            + "-fx-padding: 8 16;";

    public static final String DANGER_BUTTON =
            "-fx-background-color: #ef4444;"
            + "-fx-text-fill: white;"
            + "-fx-background-radius: 999;"
            + "-fx-padding: 8 16;";

    public static final String TEXTFIELD_DARK =
            "-fx-background-radius: 8;"
            + "-fx-background-color: #020617;"
            + "-fx-text-fill: #e5e7eb;"
            + "-fx-prompt-text-fill: #6b7280;"
            + "-fx-border-color: #1e293b;"
            + "-fx-border-radius: 8;";

    public static final String TEXTAREA_DARK = TEXTFIELD_DARK;

    public static final String SIDEBAR =
            "-fx-background-color: linear-gradient(to bottom, #020617, #020617);";

    public static final String SIDEBAR_BUTTON =
            "-fx-background-color: transparent;"
            + "-fx-text-fill: #9ca3af;"
            + "-fx-alignment: CENTER_LEFT;"
            + "-fx-padding: 10 14;"
            + "-fx-background-radius: 8;";

    public static final String SIDEBAR_BUTTON_ACTIVE =
            "-fx-background-color: rgba(37,99,235,0.3);"
            + "-fx-text-fill: #e5e7eb;"
            + "-fx-alignment: CENTER_LEFT;"
            + "-fx-padding: 10 14;"
            + "-fx-background-radius: 8;";
}
