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
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateCustomerController implements Initializable {
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
    ObservableList<FirstLevelDivision> dataForCountry = FXCollections.observableArrayList();
    ObservableList<Country> comboCountrySelection = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerToUpdate = MainController.getCustomerToUpdate();
        try {
            comboCountry.setItems(CountryQuery.getCountryFromDB());
            fldUser = FirstLevelDivision.getFirstLevelById(customerToUpdate.getDivisionId());
            comboCountry.setValue(Country.getCountryById(fldUser));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        IdTextField.setText(String.valueOf(customerToUpdate.getId()));
        nameTextField.setText(customerToUpdate.getName());
        addressTextField.setText(customerToUpdate.getAddress());
        postalCodeTextField.setText(customerToUpdate.getPostalCode());
        phoneNumberTextField.setText(customerToUpdate.getPhoneNumber());
        comboCity.setValue(fldUser);

    }

    public void onComboBox1(ActionEvent actionEvent) throws SQLException {
        comboCountrySelection.add((Country) comboCountry.getValue());
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

    public void onUpdateCustomer(ActionEvent actionEvent) {
        try {
            int id = Integer.parseInt(IdTextField.getText());
            String customerName = nameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneNumberTextField.getText();
            FirstLevelDivision fld = (FirstLevelDivision) comboCity.getValue();
            int divId = fld.getDivId();
            CustomerQuery.update(id, customerName, address, postalCode, phone, divId);
            returnToMain(actionEvent);
        } catch (Exception e) {
            //Error.errorAlert("Error", "Empty Text Fields", "Cannot have empty text fields");
            System.out.println(e);
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

    private void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }


}
