package controller;

import helper.CustomerQuery;
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
    Country countrySelected;
    ObservableList<Country> allCountries = Country.getAllCountries();
    ObservableList<FirstLevelDivision> allFirstLevelDivision = FirstLevelDivision.getAllFirstLvlDiv();
    ObservableList<FirstLevelDivision> dataForCountry = FXCollections.observableArrayList();
    ObservableList<Country> comboCountrySelection = FXCollections.observableArrayList();
    int customerSize = Customer.getAllCustomers().size();
    public void onAddCustomer(ActionEvent actionEvent) {
        try {
            int id = customerSize + 1;
            String customerName = nameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            int divID = comboCity.getValue().getDivId();
            Customer customerToAdd = new Customer(id, customerName, address, postalCode, phone, divID);
            Customer.addCustomer(customerToAdd);
            CustomerQuery.insert(id, customerName, address, postalCode, phone, divID);
            returnToMain(actionEvent);
        } catch (Exception e) {
            Error.errorAlert("Error", "Empty Text Fields", "Cannot have empty text fields");
        }

    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IdTextField.setPromptText(String.valueOf(customerSize + 1));
        comboCountry.setPromptText("You must select a country");
        comboCountry.setItems(allCountries);
        System.out.println(customerSize);


     } // end of initialize

    public void onComboBox1(ActionEvent actionEvent) {
        comboCountrySelection.add(comboCountry.getValue());
        if (comboCountrySelection.size() > 1) {
            comboCountrySelection.remove(0);
            dataForCountry.clear();
        }
        countrySelected = comboCountrySelection.get(0);
        for (FirstLevelDivision f : allFirstLevelDivision) {
            if (countrySelected.getCountryId() == f.getCountryId()) {
                dataForCountry.add(f);
            }
        }
        comboCity.setItems(dataForCountry);
    }


    public void comboBox2(ActionEvent actionEvent) {

    }

    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }
}
