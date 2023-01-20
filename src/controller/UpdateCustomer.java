package controller;

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
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {
    Stage stage;
    Parent scene;
    public TextField IdTextField;
    public TextField nameTextField;
    public TextField addressTextField;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;
    public ComboBox comboCountry;
    public ComboBox comboCity;
    Customer customerToUpdate;
    FirstLevelDivision fldUser;
    Country countrySelected;
    ObservableList<Country> allCountries = Country.getAllCountries();
    ObservableList<FirstLevelDivision> allFirstLevelDivision = FirstLevelDivision.getAllFirstLvlDiv();
    ObservableList<FirstLevelDivision> dataForCountry = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerToUpdate = MainController.getCustomerToUpdate();
        comboCountry.setItems(allCountries);
        fldUser = FirstLevelDivision.getFirstLevelById(customerToUpdate.getDivisionId());
        IdTextField.setText(String.valueOf(customerToUpdate.getId()));
        nameTextField.setText(customerToUpdate.getName());
        addressTextField.setText(customerToUpdate.getAddress());
        postalCodeTextField.setText(customerToUpdate.getPostalCode());
        phoneNumberTextField.setText(customerToUpdate.getPhoneNumber());
        comboCountry.setValue(Country.getCountryById(fldUser));
        comboCity.setValue(fldUser);

    }

    public void onComboBox1(ActionEvent actionEvent) {
        countrySelected = Country.getCountryById(fldUser);
        for (FirstLevelDivision f : allFirstLevelDivision) {
            if (countrySelected.getCountryId() == f.getCountryId()) {
                dataForCountry.add(f);
            }
        }
        comboCity.setItems(dataForCountry);
    }

    public void comboBox2(ActionEvent actionEvent) {
    }

    public void onUpdateCustomer(ActionEvent actionEvent) {
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


}
