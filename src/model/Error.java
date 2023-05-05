package model;

import javafx.scene.control.Alert;

public class Error {
    /**
     * Error info used to display to user
     * @param alertTitle
     * @param alertHeader
     * @param alertContent
     */
    public static void infoAlert(String alertTitle, String alertHeader, String alertContent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContent);
        alert.showAndWait();
    }
    /**
     * Error alert used to display to the user
     * @param alertTitle
     * @param alertHeader
     * @param alertContent
     */
    public static void errorAlert(String alertTitle, String alertHeader, String alertContent) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertContent);
        alert.showAndWait();
    }
}
