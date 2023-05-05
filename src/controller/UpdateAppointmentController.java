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
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;


public class UpdateAppointmentController implements Initializable {
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
    Appointment apptToUpdate;


    public void onClickSave(ActionEvent actionEvent) {
        try {
            Contact contactSelected = (Contact) apptContactComboBox.getValue();
            Customer customerSelected = (Customer) apptCustomerIdComboBox.getValue();
            User userSelected = (User) apptUserIdComboBox.getValue();
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
            if (checkDate(startDate) || checkDate(endDate)) {
                Error.infoAlert("Error", "Closed on Weekends", "Offices are closed on the Weekends");
            } else if (!checkHours(startTime) || !checkHours(endTime)) {
                Error.infoAlert("Error", "Appointment outside office hours", "Appointment is outside office hours of " +
                        "8am et and 10pm et");
            }else if (checkForAppt(dtStart, dtEnd, customerId, apptToUpdate.getId())) {
                Error.infoAlert("Error", "Appointment Overlap", "Error Appointment is overlapping with another appointment");
            } else if(dtEnd.isBefore(dtStart) ) {
                Error.infoAlert("Error", "End date must be after start date", "Please pick a new end date");
            } else {
                //ZonedDateTime zonedStart = dtStart.atZone(ZoneId.systemDefault());
                //ZonedDateTime utcZonedStart = zonedStart.withZoneSameInstant(ZoneId.of("UTC"));
                //ZonedDateTime zonedEnd = dtEnd.atZone(ZoneId.systemDefault());
                //ZonedDateTime utcZonedEnd = zonedEnd.withZoneSameInstant(ZoneId.of("UTC"));
                //System.out.println(dtStart + " " + dtEnd);
                //Appointment appt = new Appointment(apptToUpdate.getId(), title,description,location,type,dtStart,dtEnd,customerId,userId,contactId);
                //Appointment.updateAppointment(appt);
                AppointmentQuery.update(apptToUpdate.getId(), title, description, location, type, dtStart, dtEnd, customerId, userId, contactId);
                returnToMain(actionEvent);
            }
        } catch (Exception e) {
            System.out.println(e);
            //Error.errorAlert("Error", "Empty Text Fields", "Cannot have empty text fields");
        }
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apptToUpdate = MainController.getApptToUpdate();
        int contactId = apptToUpdate.getContactId();
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(23, 30);
        while (start.isBefore(end.plusSeconds(1))){
            apptStartTimeComboBox.getItems().add(start);
            apptEndTimeComboBox.getItems().add(start);
            start = start.plusMinutes(15);
        }
        try {
            apptUserIdComboBox.setItems(UserQuery.getUserFromDB());
            apptCustomerIdComboBox.setItems(CustomerQuery.getCustomerFromDB());
            apptContactComboBox.setItems(ContactQuery.getContactFromDB());
            for (Contact c : ContactQuery.getContactFromDB()) {
                if (c.getContactId() == contactId) {
                    apptContactComboBox.setValue(c);
                }
            }
            for (Customer c : CustomerQuery.getCustomerFromDB()) {
                if (c.getId() == apptToUpdate.getCustomerId()) {
                    apptCustomerIdComboBox.setValue(c);
                }
            }
            for (User u : UserQuery.getUserFromDB()) {
                if (u.getUserId() == apptToUpdate.getUserId()) {
                    apptUserIdComboBox.setValue(u);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        apptIdTextField.setText(String.valueOf(apptToUpdate.getId()));
        apptTitleTextField.setText(apptToUpdate.getTitle());
        apptDescriptionTextField.setText(apptToUpdate.getDescription());
        apptLocationTextField.setText(apptToUpdate.getLocation());
        apptTypeTextField.setText(apptToUpdate.getType());
        apptStartDate.setValue(LocalDate.from(apptToUpdate.getStartDate()));
        apptStartTimeComboBox.setValue(LocalTime.from(apptToUpdate.getStartDate()));
        apptEndDate.setValue(LocalDate.from(apptToUpdate.getEndDate()));
        apptEndTimeComboBox.setValue(LocalTime.from(apptToUpdate.getEndDate()));
    }

    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }
    public static boolean checkDate(LocalDate localDate) {
        //String dayOfWeek = ZonedDateTime.now().getDayOfWeek().toString();
        return "SATURDAY".equalsIgnoreCase(String.valueOf(localDate)) || "SUNDAY".equalsIgnoreCase(String.valueOf(localDate));
    }
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

    public static boolean checkForAppt(LocalDateTime startTimeToCheck, LocalDateTime endTimeToCheck, int customerIdToCheck, int apptId) throws SQLException {
        for (Appointment appt : AppointmentQuery.getAppointmentFromDB()) {
            if (customerIdToCheck != appt.getCustomerId() || apptId == appt.getId()) {
                continue;
            }
            LocalDateTime startTime = appt.getStartDate();
            LocalDateTime endTime = appt.getEndDate();
                if (startTimeToCheck.isAfter(startTime) && startTimeToCheck.isBefore(endTime)) {
                    return true;
                } else if (endTimeToCheck.isAfter(startTime) && endTimeToCheck.isBefore(endTime)) {
                    return true;
                } else if ((startTime.isBefore(startTime) && endTimeToCheck.isEqual(endTime))) {
                    return true;
                } else if ((startTimeToCheck.isEqual(startTime) ||
                        endTimeToCheck.isEqual(endTime))) {
                    return true;
                }
        }
        return false;
    }
}

