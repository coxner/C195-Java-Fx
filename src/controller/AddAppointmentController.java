package controller;

import helper.AppointmentQuery;
import helper.ContactQuery;
import helper.CustomerQuery;
import helper.UserQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.Error;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddAppointmentController implements Initializable {
    Stage stage;
    Parent scene;
    
    public TextField apptIdTextField;
    public TextField apptTitleTextField;
    public TextField apptDescriptionTextField;
    public TextField apptLocationTextField;
    public TextField apptTypeTextField;
    public ComboBox apptContactComboBox;

    public DatePicker apptStartDate;
    public ComboBox apptCustomerIdComboBox;
    public ComboBox apptUserIdComboBox;
    public DatePicker apptEndDate;
    public ComboBox apptEndTimeComboBox;
    public ComboBox apptStartTimeComboBox;


    /**
     * New appointment is created with all helper methods being called to make sure appointment meets conditions set and
     * upon appointment creation it is sent to the database
     * @param actionEvent user clicks save button
     */
    public void onClickSave(ActionEvent actionEvent) {
        try {
            Contact contactSelected = (Contact) apptContactComboBox.getValue();
            Customer customerSelected = (Customer) apptCustomerIdComboBox.getValue();
            User userSelected = (User) apptUserIdComboBox.getValue();
            //int id = Integer.parseInt(apptIdTextField.getText());
            String title = apptTitleTextField.getText();
            String description = apptDescriptionTextField.getText();
            String location = apptLocationTextField.getText();
            int contactId = contactSelected.getContactId();
            String type = apptTypeTextField.getText();
            LocalDate startDate = apptStartDate.getValue();
            LocalTime startTime = (LocalTime) apptStartTimeComboBox.getValue();
            LocalDateTime dtStart = LocalDateTime.of(startDate, startTime);
            LocalDate endDate = apptEndDate.getValue();
            LocalTime endTime = (LocalTime) apptEndTimeComboBox.getValue();
            LocalDateTime dtEnd = LocalDateTime.of(endDate, endTime);
            int customerId = customerSelected.getId();
            int userId = userSelected.getUserId();
            System.out.println(dtStart.atZone(ZoneId.of("US/Eastern")));
            if (checkDate(startDate) || checkDate(endDate)) {
            Error.infoAlert("Error", "Closed on Weekends", "Offices are closed on the Weekends");
             } else if (!checkHours(startTime) || !checkHours(endTime)) {
                Error.infoAlert("Error", "Appointment outside office hours", "Appointment is outside office hours of " +
                        "8am et and 10pm et");
            }else if (checkForAppt(dtStart, dtEnd, customerId)) {
            Error.infoAlert("Error", "Appointment Overlap", "Error Appointment is overlapping with another appointment");
            } else if(dtEnd.isBefore(dtStart) ) {
                Error.infoAlert("Error", "End date must be after start date", "Please pick a new end date");
            }
             else {
            AppointmentQuery.insert(title, description, location, type, dtStart, dtEnd, customerId, userId, contactId);
            returnToMain(actionEvent);
        }
        } catch (Exception e) {
            System.out.println(e);
            Error.errorAlert("Error", "Empty Text Fields", "Cannot have empty text fields");
        }
    }

    /**
     * Returns user to main menu when exit is clicked displays a Confirmation alert warning user any unsaved changes to
     * appointment will be lost
     * @param actionEvent user clicks exit to return to main menu
     * @throws IOException from the FXMLLLoader
     */
    public void onClickExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to return to main menu?  Any unsaved changes will be lost");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            this.scene = FXMLLoader.load(this.getClass().getResource("/view/Main.fxml"));
            this.stage.setScene(new Scene(this.scene));
            this.stage.show();
        }
    }

    /**
     * Method stores user selection in an Observable list to allow program to dynamically change end time based on a user selection.
     * When user sets a start time end time combo box is filled with times after selection.

     */
    /*
    public void setStartTime(ActionEvent actionEvent) {
        localTimeSelected.add((LocalTime) apptStartTimeComboBox.getValue());
        if (localTimeSelected.size() > 1) {
            localTimeSelected.remove(0);
            endTime.clear();
        }
        LocalTime start = localTimeSelected.get(0).plusMinutes(15);
        LocalTime end = LocalTime.of(23, 30);
        while (start.isBefore(end.plusSeconds(1))){
            endTime.add(start);
            start = start.plusMinutes(15);
        }
        apptEndTimeComboBox.setItems(endTime);


    }*/

    public static boolean checkHours(LocalTime startTime) {
        boolean isValid = false;
        LocalDate startDate = LocalDate.now();
        ZoneId localZoneId = ZoneId.systemDefault();
        ZoneId estZoneID = ZoneId.of("US/Eastern");
        ZonedDateTime localZDT = ZonedDateTime.of(startDate, startTime, localZoneId);
        ZonedDateTime etTime = ZonedDateTime.ofInstant(localZDT.toInstant(), estZoneID);
        LocalTime bStart = LocalTime.of(8,0);
        LocalTime bEnd = LocalTime.of(22,0);
        if (etTime.toLocalTime().isAfter(bStart) && etTime.toLocalTime().isBefore(bEnd)) {
            isValid = true;
        }
        return isValid;
    }
    /**
     * Method used in if statement when adding an appointment to check if date entered is on the weekend
     * @param localDate date to be checked
     * @return true or false to be used in if else
     */
    public static boolean checkDate(LocalDate localDate) {
        //String dayOfWeek = ZonedDateTime.now().getDayOfWeek().toString();
        return "SATURDAY".equalsIgnoreCase(String.valueOf(localDate)) || "SUNDAY".equalsIgnoreCase(String.valueOf(localDate));
    }

    /**
     * Method used to check if appointments overlap
     * @param startTimeToCheck start time being checked for overlap
     * @param endTimeToCheck end time being check for overlap
     * @param customerIdToCheck customerId being checked
     * @return
     */
    public static boolean checkForAppt(LocalDateTime startTimeToCheck, LocalDateTime endTimeToCheck, int customerIdToCheck) throws SQLException {
        for (Appointment appt : AppointmentQuery.getAppointmentFromDB()) {
            if (customerIdToCheck != appt.getCustomerId()) {
                continue;
            }
            LocalDateTime startTime = appt.getStartDate();
            LocalDateTime endTime = appt.getEndDate();
            //internal
            if (startTimeToCheck.isAfter(startTime) && startTimeToCheck.isBefore(endTime)) {
                return true;
                //
            } else if (endTimeToCheck.isAfter(startTime) && endTimeToCheck.isBefore(endTime)) {
                return true;
            } else if ((startTime.isBefore(startTime) && endTimeToCheck.isAfter(endTime))) {
                return true;
            } else if ((startTimeToCheck.isEqual(startTime) ||
                    endTimeToCheck.isEqual(endTime))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Run when controller is launched sets all combo box and text-field values
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);
        while (start.isBefore(end.plusSeconds(1))){
            apptStartTimeComboBox.getItems().add(start);
            apptEndTimeComboBox.getItems().add(start);
            start = start.plusMinutes(15);
        }
        try {
            apptCustomerIdComboBox.setItems(CustomerQuery.getCustomerFromDB());
            apptContactComboBox.setItems(ContactQuery.getContactFromDB());
            apptUserIdComboBox.setItems(UserQuery.getUserFromDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //apptIdTextField.setText(Integer.toString(allAppointments.size() + 1));

    }

    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }
}
