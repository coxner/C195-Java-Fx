package controller;

import helper.AppointmentQuery;
import helper.UserQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Error;
import model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static model.Appointment.allAppointments;


public class LoginController implements Initializable {
    public Label usernameLbl;
    public Label passwordLbl;
    public Button loginBtn;
    public Button exitBtn;
    Stage stage;
    Parent scene;
    // Variables for login form
    public TextField passwordTextField;
    public TextField usernameTextField;
    public Label locationLbl;
    public LocalDateTime timeOfAppt;
    LocalDateTime timeForAppt;
    public int apptId;
    public boolean appointmentSoon = false;



    public void onLoginClicked(ActionEvent actionEvent) throws IOException, SQLException {
        LocalDateTime addToCurrent = LocalDateTime.now().plusMinutes(15);
        LocalDateTime subractFromCurrent = LocalDateTime.now().minusMinutes(15);
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        String userName = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (User.verifyUser(userName, password)) {
            returnToMain(actionEvent);
            outputFile.println("User: " + userName + " logged into system at: " + Timestamp.valueOf(LocalDateTime.now()));
            for(Appointment appt : allAppointments) {
                timeOfAppt = appt.getStartDate();
                if (timeOfAppt.isAfter(subractFromCurrent) && timeOfAppt.isBefore(addToCurrent)) {
                    apptId = appt.getId();
                    timeForAppt = timeOfAppt;
                    appointmentSoon = true;
                }
            }
            if(appointmentSoon) {
                Error.infoAlert("Warning Appointment", "Appointment Soon", "Appointment " + apptId
                        + " is at " + timeForAppt);
            } else {
                Error.infoAlert("No Appontments Soon", "No Upcoming Appointments", "No appointments in the next 15 minutes");
            }
        } else if(!User.verifyUser(userName,password)) {
            Error.infoAlert("Login Failed", "Login was not Successful", "Please enter a valid username and password");
            outputFile.println("User: " + userName + " logged into system at: " + Timestamp.valueOf(LocalDateTime.now()));
        }
        outputFile.close();
    }

    public void onExitClicked(ActionEvent actionEvent) {
    }
    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Locale localSystem = Locale.getDefault();
        Locale.setDefault(localSystem);


        ZoneId zone = ZoneId.of(TimeZone.getDefault().getID());
        locationLbl.setText(String.valueOf(zone));
        for (Appointment appt : allAppointments) {
            ZonedDateTime zonedStartDateTime = appt.getStartDate().atZone(ZoneId.of("UTC"));
            ZonedDateTime setZoneStartTime = zonedStartDateTime.withZoneSameInstant(zone);
            appt.setStartDate(setZoneStartTime.toLocalDateTime());
            ZonedDateTime zonedEndTime = appt.getEndDate().atZone(ZoneId.of("UTC"));
            ZonedDateTime setZonedEndTime = zonedEndTime.withZoneSameInstant(zone);
            appt.setEndDate(setZonedEndTime.toLocalDateTime());
        }

        //ResourceBundle rb = ResourceBundle.getBundle("language/login", localSystem);
        //usernameLbl.setText(rb.getString("Login"));
        
    }
}
