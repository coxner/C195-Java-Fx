package controller;

import helper.CountryQuery;
import helper.CustomerQuery;
import helper.FirstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Error;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {
    Stage stage;
    Parent scene;
    public TextField IdTextField;
    public TextField nameTextField;
    public TextField addressTextField;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;
    public ComboBox<Country> comboCountry;
    public ComboBox<FirstLevelDivision> comboCity;
    ObservableList<FirstLevelDivision> dataForCountry = FXCollections.observableArrayList();
    ObservableList<Country> comboCountrySelection = FXCollections.observableArrayList();
    Country countrySelected;

    /**
     * User clicks save button customer is saved and customer is also sent to the database
     * @param actionEvent user clicks save
     */
    public void onAddCustomer(ActionEvent actionEvent) {
        try {
            String customerName = nameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            int divID = comboCity.getValue().getDivId();
            CustomerQuery.insert(customerName, address, postalCode, phone, divID);
            returnToMain(actionEvent);
        } catch (Exception e) {
            Error.errorAlert("Error", "Empty Text Fields", "Cannot have empty text fields");
        }

    }

    /**
     * User cancels creating a new customer Confirmation alert is thrown as a warning changes will not be saved
     * @param actionEvent user clicks cancel to return to main screen
     * @throws IOException IOException from the FXMLLLoader
     */
    public void onCancelCustomer(ActionEvent actionEvent) throws IOException {
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
     * Observable list used to store value entered into the combo box then sets the following combo bow with states / provinces
     * of country that user choices
     * @param actionEvent user selects a country
     */
    public void onComboBox1(ActionEvent actionEvent) throws SQLException {
        comboCountrySelection.add(comboCountry.getValue());
        if (comboCountrySelection.size() > 1) {
            comboCountrySelection.remove(0);
            dataForCountry.clear();
        }
        countrySelected = comboCountrySelection.get(0);
        for (FirstLevelDivision f : FirstLevelDivisionQuery.getFirstLevelDivisionFromDB()) {
            if (countrySelected.getCountryId() == f.getCountryId()) {
                dataForCountry.add(f);
            }
        }
        comboCity.setItems(dataForCountry);
    }

    /**
     * Called when controller launches sets customer id and sets combo box values
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
        comboCountry.setPromptText("You must select a country");
        try {
            comboCountry.setItems(CountryQuery.getCountryFromDB());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // end of initialize

    /**
     * Helper method used to return to the main menu when action event occurs
     * @param actionEvent user wants to return to main menu
     * @throws IOException IOException from the FXMLLLoader
     */
    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }
}
