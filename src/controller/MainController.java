package controller;

import helper.AppointmentQuery;
import helper.CustomerQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import model.Error;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Appointment.allAppointments;
import static model.Customer.allCustomers;

public class MainController implements Initializable {
    Stage stage;
    Parent scene;
    // Customer Table
    public TableView customerTable;
    public TableColumn customerIdCol;
    public TableColumn customerNameCol;
    public TableColumn customerAddressCol;
    public TableColumn customerPostalCodeCol;
    public TableColumn customerPhoneCol;
    // Appointments Table
    public TableColumn apptIdCol;
    public TableColumn apptCustomerIdCol;
    public TableColumn apptTitle;
    public TableColumn apptStartDate;
    public TableColumn apptEndDate;
    public TableColumn apptLocationCol;
    public TableView appointmentTable;
    private  static Customer customerToUpdate;

    public static Customer getCustomerToUpdate() {return customerToUpdate;}
    public void onClickAddCustomer(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/AddCustomer.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    public void onClickUpdateCustomer(ActionEvent actionEvent) throws IOException {
        customerToUpdate = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (customerToUpdate == null) {
            Error.errorAlert("Error Alert", "No customer Selected", "Choose a customer from the table");
        } else {
            this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            this.scene = FXMLLoader.load(this.getClass().getResource("/view/UpdateCustomer.fxml"));
            this.stage.setScene(new Scene(this.scene));
            this.stage.show();
        }
    }

    public void onClickDeleteCustomer(ActionEvent actionEvent) throws SQLException {
        Customer customerToDelete = (Customer) customerTable.getSelectionModel().getSelectedItem();
        if (customerTable.getSelectionModel().isEmpty()) {
            Error.infoAlert("Error", "No Customer Selected", "Choose a Customer from the table");
        } else if (checkForAppt(customerToDelete)) {
            Error.infoAlert("Error", "Customer can't have associated appts", "Delete appts then retry");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you wish to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int customerIdToDelete = customerToDelete.getId();
                Customer.deleteCustomer(customerToDelete);
                CustomerQuery.deleteCustomer(customerIdToDelete);
            }
        }
    }

    public boolean checkForAppt(Customer c) {
        for (Appointment appt : allAppointments) {
            if (c.getId() == appt.getCustomerId()) {
                return true;
            }
        }
        return false;
    }

    public void onAddAppt(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/AddAppointment.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();

    }

    public void onUpdateAppt(ActionEvent actionEvent) {
    }

    public void onDeleteAppt(ActionEvent actionEvent) throws SQLException {
        Appointment appointmentToDelete = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (appointmentTable.getSelectionModel().isEmpty()) {
            Error.infoAlert("Error", "No Appointment Selected", "Choose a Appointment from the table");
        }  else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Are you sure you wish to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int appointmentIdToDelete = appointmentToDelete.getId();
                Appointment.deleteAppointment(appointmentToDelete);
                AppointmentQuery.deleteAppointment(appointmentIdToDelete);
            }
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {



        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerTable.setItems(allCustomers);


        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        apptEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        appointmentTable.setItems(allAppointments);


    }
}
