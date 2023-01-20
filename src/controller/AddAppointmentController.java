package controller;

import helper.AppointmentQuery;
import helper.ContactQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;
import static model.Appointment.allAppointments;

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
    ObservableList<User> allUsers = User.getAllUsers();
    ObservableList<Customer> allCustomers = Customer.getAllCustomers();
    ObservableList<LocalTime> localTimeSelected = FXCollections.observableArrayList();
    ObservableList<LocalTime> endTime = FXCollections.observableArrayList();
    ObservableList<Contact> allContacts;
    {
        try {
            allContacts = ContactQuery.getContactFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(20, 0);
        while (start.isBefore(end.plusSeconds(1))){
            apptStartTimeComboBox.getItems().add(start);
            start = start.plusMinutes(15);
        }
        apptUserIdComboBox.setItems(allUsers);
        apptCustomerIdComboBox.setItems(allCustomers);
        apptContactComboBox.setItems(allContacts);
        apptIdTextField.setText(Integer.toString(allAppointments.size() + 1));


    }

    public void onClickSave(ActionEvent actionEvent) {
        try {
            Contact contactSelected = (Contact) apptContactComboBox.getValue();
            Customer customerSelected = (Customer) apptCustomerIdComboBox.getValue();
            User userSelected = (User) apptUserIdComboBox.getValue();
            int id = Integer.parseInt(apptIdTextField.getText());
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
             } else if (checkForAppt(dtStart, dtEnd, customerId)) {
            Error.infoAlert("Error", "Appointment Overlap", "Error Appointment is overlapping with another appointment");
            } else {
            ZonedDateTime zonedStart = dtStart.atZone(ZoneId.systemDefault());
            ZonedDateTime utcZonedStart = zonedStart.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime zonedEnd = dtEnd.atZone(ZoneId.systemDefault());
            ZonedDateTime utcZonedEnd = zonedEnd.withZoneSameInstant(ZoneId.of("UTC"));
            System.out.println(utcZonedStart);
            AppointmentQuery.insert(id, title, description, location, type, utcZonedStart.toLocalDateTime(), utcZonedEnd.toLocalDateTime(), customerId, userId, contactId);
            Appointment appt = new Appointment(id, title,description,location,type,dtStart,dtEnd,customerId,userId,contactId);
            allAppointments.add(appt);
            returnToMain(actionEvent);
        }
        } catch (Exception e) {
            Error.errorAlert("Error", "Empty Text Fields", "Cannot have empty text fields");
            System.out.println(e);
        }
    }

    public void onClickExit(ActionEvent actionEvent) {
        System.out.println(checkDate(apptStartDate.getValue()));
    }

    public void setStartTime(ActionEvent actionEvent) {
        localTimeSelected.add((LocalTime) apptStartTimeComboBox.getValue());
        if (localTimeSelected.size() > 1) {
            localTimeSelected.remove(0);
            endTime.clear();
        }
        LocalTime start = localTimeSelected.get(0).plusMinutes(15);
        LocalTime end = LocalTime.of(20, 0);
        while (start.isBefore(end.plusSeconds(1))){
            endTime.add(start);
            start = start.plusMinutes(15);
        }
        apptEndTimeComboBox.setItems(endTime);

    }

    public static boolean checkDate(LocalDate localDate) {
        String dayOfWeek = ZonedDateTime.now().getDayOfWeek().toString();
            if("SATURDAY".equalsIgnoreCase(dayOfWeek) || "SUNDAY".equalsIgnoreCase(dayOfWeek)) {
                return true;
            }
            return false;
    }

    public static boolean checkForAppt(LocalDateTime startTime, LocalDateTime endTime, int customerIdToCheck) {
        for (Appointment appt : allAppointments) {
            LocalDateTime startTimeToCheck = appt.getStartDate();
            LocalDateTime endTimeToCheck = appt.getEndDate();
            if (customerIdToCheck == appt.getCustomerId() && startTime.isBefore(startTimeToCheck) && endTime.isAfter(endTimeToCheck)) {
                return true;
            }
        }
        return false;
    }
    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }
}
