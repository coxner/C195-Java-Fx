package controller;

import helper.AppointmentQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import model.Appointment;
import model.Error;
import model.User;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

import java.util.*;




public class LoginController implements Initializable {
    public Label usernameLbl;
    public Label passwordLbl;
    public Button loginBtn;
    public Button exitBtn;
    public Label titleLbl;

    Stage stage;
    Parent scene;
    // Variables for login form
    public TextField passwordTextField;
    public TextField usernameTextField;
    public Label locationLbl;
    /**
     * Local variables for controller
     */
    public LocalDateTime timeOfAppt;
    LocalDateTime timeForAppt;
    public int apptId;
    public boolean appointmentSoon = false;

    /**
     * User enters a username and password and goes to log in helper methods are called to verify the user. Login attempt
     * is written to a txt file. Checks if user has an upcoming appointment and if they do an info alert is displayed.
     *
     * @param actionEvent when user clicks to log in
     * @throws IOException  IOException from the FXMLLLoader
     * @throws SQLException
     */
    public void onLoginClicked(ActionEvent actionEvent) throws IOException, SQLException {
        LocalDateTime addToCurrent = LocalDateTime.now().plusMinutes(15);
        LocalDateTime subractFromCurrent = LocalDateTime.now().minusMinutes(15);
        ResourceBundle rb = ResourceBundle.getBundle("language/login", Locale.getDefault());
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        String userName = usernameTextField.getText();
        String password = passwordTextField.getText();
        if (User.verifyUser(userName, password)) {
            returnToMain(actionEvent);
            outputFile.println("User: " + userName + " logged into system at: " + Timestamp.valueOf(LocalDateTime.now()));
            for (Appointment appt : AppointmentQuery.getAppointmentFromDB()) {
                timeOfAppt = appt.getStartDate();
                if (timeOfAppt.isAfter(subractFromCurrent) && timeOfAppt.isBefore(addToCurrent)) {
                    apptId = appt.getId();
                    timeForAppt = timeOfAppt;
                    appointmentSoon = true;
                }
            }
            if (appointmentSoon) {
                Error.infoAlert("Warning Appointment", "Appointment Soon", "Appointment " + apptId
                        + " is at " + timeForAppt);
            } else {
                Error.infoAlert("No Appointments Soon", "No Upcoming Appointments", "No appointments in the next 15 minutes");
            }
        } else if (!User.verifyUser(userName, password)) {
            Error.infoAlert(rb.getString("error"), "Login was not Successful", rb.getString("incorrect"));
            outputFile.println("User: " + userName + " failed to log into system at: " + Timestamp.valueOf(LocalDateTime.now()));
        }
        outputFile.close();
    }

    /**
     * Exits program but displays alert first warning the user
     *
     * @param actionEvent user wants to exit program
     */
    public void onExitClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Helper method used to return to the main menu when action event occurs is used throughout the program
     *
     * @param actionEvent user wants to return to main menu
     * @throws IOException IOException from the FXMLLLoader
     */
    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    /**
     * Ran when login screen is launched gets users location for time zone and language properties information
     *
     * @param location The location used to resolve relative paths for the root object, or
     *                 {@code null} if the location is not known.
     * @param rb       The resources used to localize the root object, or {@code null} if
     *                 the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        Locale localSystem = Locale.getDefault();
        Locale.setDefault(localSystem);
        ZoneId zone = ZoneId.of(TimeZone.getDefault().getID());

        try {
            locationLbl.setText(String.valueOf(zone));
            rb = ResourceBundle.getBundle("language/login", Locale.getDefault());
            titleLbl.setText(rb.getString("title"));
            usernameLbl.setText(rb.getString("username"));
            passwordLbl.setText(rb.getString("password"));
            loginBtn.setText(rb.getString("loginBtn"));
            exitBtn.setText(rb.getString("exitBtn"));

        } catch (MissingResourceException e) {
            System.out.println(e);
        }

        /*for (Appointment appt : allAppointments) {
            ZonedDateTime zonedStartDateTime = appt.getStartDate().atZone(ZoneId.of("UTC"));
            System.out.println(zonedStartDateTime);
            ZonedDateTime setZoneStartTime = zonedStartDateTime.withZoneSameInstant(zone);
            System.out.println(setZoneStartTime);
            appt.setStartDate(setZoneStartTime.toLocalDateTime());
            ZonedDateTime zonedEndTime = appt.getEndDate().atZone(ZoneId.of("UTC"));
            ZonedDateTime setZonedEndTime = zonedEndTime.withZoneSameInstant(zone);
            appt.setEndDate(setZonedEndTime.toLocalDateTime());
        }*/
    }
}


