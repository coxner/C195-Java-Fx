package controller;

import helper.AppointmentQuery;
import helper.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.WeekFields;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Calendar;



public class MainController implements Initializable {
    public TableColumn apptUserIdCol;
    public TableColumn apptContactCol;
    public TableColumn apptDescriptionCol;
    public RadioButton allApptsBtn;
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
    // Local variables
    private static Appointment apptToUpdate;
    private  static Customer customerToUpdate;
    public static int buttonClicked;

    /**
     * Get the customer to update
     * @return cbustomer to update
     */
    public static Customer getCustomerToUpdate() {return customerToUpdate;}

    /**
     * get appointment to update
     * @return appointment to update
     */
    public static Appointment getApptToUpdate() {return apptToUpdate;}

    /**
     * Opens add a customer controller
     * @param actionEvent user clicks to add a customer to the program
     * @throws IOException IOException from the FXMLLLoader
     */
    public void onClickAddCustomer(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/AddCustomer.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    /**
     * Assign value to customerToUpdate to pass it to update custoemr controller
     * @param actionEvent user selects a customer to update
     * @throws IOException IOException from the FXMLLLoader
     */
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

    /**
     * Customer is deleted from the program and the database
     * @param actionEvent user deletes a customer
     * @throws SQLException
     */
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
                CustomerQuery.deleteCustomer(customerToDelete.getId());
            }
        }
    }

    /**
     * Helper method to check if a customer being deleted has appointed tied to them
     * @param c customer being passed in
     * @return boolean value indicating if customer has appointments tied to them
     */
    public boolean checkForAppt(Customer c) throws SQLException {
        for (Appointment appt : AppointmentQuery.getAppointmentFromDB()) {
            if (c.getId() == appt.getCustomerId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Opens add appointment controller
     * @param actionEvent user wants to add an appointment to program
     * @throws IOException IOException from the FXMLLLoader
     */
    public void onAddAppt(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/AddAppointment.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    /**
     * Assign value to apptToUpdate it to pass it across controllers and load the update appointment controller
     * @param actionEvent user selects an appointment to update
     * @throws IOException IOException from the FXMLLLoader
     */
    public void onUpdateAppt(ActionEvent actionEvent) throws IOException {
        apptToUpdate = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();
        if (apptToUpdate == null) {
            Error.errorAlert("Error Alert", "No customer Selected", "Choose a appointment from the table");
        } else {
            this.stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            this.scene = FXMLLoader.load(this.getClass().getResource("/view/UpdateAppointment.fxml"));
            this.stage.setScene(new Scene(this.scene));
            this.stage.show();
        }
    }

    /**
     * Deletes an appointment from the database and the program display information on appointment that was deleted
     * @param actionEvent user deletes a appointment
     * @throws SQLException
     */
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
                //Appointment.deleteAppointment(appointmentToDelete);
                AppointmentQuery.deleteAppointment(appointmentIdToDelete);
                try {
                    appointmentTable.setItems(AppointmentQuery.getAppointmentFromDB());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Error.infoAlert("Appointment Cancelled", "Appointment Cancelled", "The appointment with an id of " +
                        appointmentIdToDelete + " of type " + appointmentToDelete.getType() + " has been deleted");
            }

        }
    }

    /**
     * Sets the appointments table to appointments this month **USE OF FIRST LAMBDA EXPRESSION**
     * @param actionEvent User selects appointments this month's radio button
     */
    public void apptByMonthRtbn(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> apptByMonth = FXCollections.observableArrayList();
        Month currentMonth = LocalDate.now().getMonth();
        AppointmentQuery.getAppointmentFromDB().forEach(appt -> { if (appt.getStartDate().getMonth() == currentMonth) {
            apptByMonth.add(appt);
        }});
        appointmentTable.setItems(apptByMonth);
    }

    /**
     * Sets the appointments table to appointments this week **SECOND USE OF LAMBDA EXPRESSION**
     * @param actionEvent User clicks radio button to view appointments this week
     */
    public void apptByWeekRtbn(ActionEvent actionEvent) throws SQLException {
            ObservableList<Appointment> apptByWeek = FXCollections.observableArrayList();
            WeekFields week = WeekFields.ISO;
            int weekOf = Calendar.WEEK_OF_YEAR;
            AppointmentQuery.getAppointmentFromDB().forEach(appt -> {int weekNumber = appt.getStartDate().get(week.weekOfWeekBasedYear());
            if(weekNumber == weekOf) {apptByWeek.add(appt);}});
            appointmentTable.setItems(apptByWeek);
    }

    /**
     * Sets appointments table to all appointments
     * @param actionEvent user selects all appointments radio buttom
     */
    public void allApptsBtn(ActionEvent actionEvent) throws SQLException {
        appointmentTable.setItems(AppointmentQuery.getAppointmentFromDB());
    }

    /**
     * Count appointment type button is clicked
     * @param actionEvent user clicks the count appointment by type button
     * @throws IOException IOException from the FXMLLLoader
     */
    public void countApptByType(ActionEvent actionEvent) throws IOException {
        buttonClicked = 0;
        getResultsScreen(actionEvent);
    }

    /**
     * Gets the result of the button clicked to use on results controller
     * @return button clicked
     */
    public static int getButtonClicked() {
        return buttonClicked;
    }

    /**
     * Loads the results controller
     * @param actionEvent user wants to view a result
     * @throws IOException IOException from the FXMLLLoader
     */
    private void getResultsScreen(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/Results.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }

    /**
     * User wants to view appointments for contact 1
     * @param actionEvent view appointments for user
     * @throws IOException IOException from the FXMLLLoader
     */
    public void contactIdOne(ActionEvent actionEvent) throws IOException {
        buttonClicked = 1;
        getResultsScreen(actionEvent);
    }
    /**
     * User wants to view appointments for contact 2
     * @param actionEvent view appointments for user
     * @throws IOException IOException from the FXMLLLoader
     */
    public void contactIdTwo(ActionEvent actionEvent) throws IOException {
        buttonClicked = 2;
        getResultsScreen(actionEvent);
    }
    /**
     * User wants to view appointments for contact 3
     * @param actionEvent view appointments for user
     * @throws IOException IOException from the FXMLLLoader
     */
    public void contactIdThree(ActionEvent actionEvent) throws IOException {
        buttonClicked = 3;
        getResultsScreen(actionEvent);
    }

    /**
     * User clicks to view results of provinces / states in each country
     * @param actionEvent user clicks view countries province / state button
     * @throws IOException IOException from the FXMLLLoader
     */
    public void viewCountryCount(ActionEvent actionEvent) throws IOException {
        buttonClicked = 4;
        getResultsScreen(actionEvent);
    }
    public void onViewMonth(ActionEvent actionEvent) throws IOException {
        buttonClicked =5;
        getResultsScreen(actionEvent);
    }

    /**
     * Sets the table values when controller is launched
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        apptEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        apptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        try {
            appointmentTable.setItems(AppointmentQuery.getAppointmentFromDB());
            customerTable.setItems(CustomerQuery.getCustomerFromDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
