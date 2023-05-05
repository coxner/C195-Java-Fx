package controller;

import helper.ReportsQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    Stage stage;
    Parent scene;
    public TableView resultsTable;
    public TableColumn colOne;
    public TableColumn colTwo;
    public TableColumn colThree;
    public TableColumn colFour;
    public TableColumn colFive;
    public TableColumn colSix;
    public TableColumn colSeven;
    ObservableList<ResultsSetOne> resultSetOne = FXCollections.observableArrayList();
    ObservableList<ResultSetTwo> resultSetTwo = FXCollections.observableArrayList();
    ObservableList<ResultSetThree> resultSetThree = FXCollections.observableArrayList();
    ObservableList<ResultClassMonth> resultClassMonths = FXCollections.observableArrayList();

    /**
     * Set the results table to the button user clicked in main
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
        int value = MainController.getButtonClicked();
        if (value == 0) {
            try {
                resultSetOne = ReportsQuery.getAppointmentTypeQuery();
                colOne.setCellValueFactory(new PropertyValueFactory<>("resultCount"));
                colTwo.setCellValueFactory(new  PropertyValueFactory<>("type"));
                colOne.setText("# of Appt");
                colTwo.setText("Appointment Type");
                resultsTable.setItems(resultSetOne);

            } catch (SQLException e) {
                System.out.println(e);
            }

        } else if (value == 1 || value == 2 || value == 3) {
            try {
                resultSetTwo = ReportsQuery.getAppointmentForContact(value);
                colOne.setCellValueFactory(new PropertyValueFactory<>("apptId"));
                colTwo.setCellValueFactory(new PropertyValueFactory<>("title"));
                colThree.setCellValueFactory(new PropertyValueFactory<>("type"));
                colFour.setCellValueFactory(new PropertyValueFactory<>("description"));
                colFive.setCellValueFactory(new PropertyValueFactory<>("startDate"));
                colSix.setCellValueFactory(new PropertyValueFactory<>("endDate"));
                colSeven.setCellValueFactory(new PropertyValueFactory<>("customerId"));
                colOne.setText("Appt Id");
                colTwo.setText("Title");
                colThree.setText("Type");
                colFour.setText("Description");
                colFive.setText("Start");
                colSix.setText("End");
                colSeven.setText("Customer ID");
                resultsTable.setItems(resultSetTwo);
            } catch (SQLException e) {
                System.out.println(e);
            }

        } else if (value == 4) {
            try {
                resultSetThree = ReportsQuery.getCountCountry();
                colOne.setCellValueFactory(new PropertyValueFactory<>("countryName"));
                colTwo.setCellValueFactory(new  PropertyValueFactory<>("countForCountry"));
                colOne.setText("Country");
                colTwo.setText("# of Province/ States");
                resultsTable.setItems(resultSetThree);

            } catch (SQLException e) {
                System.out.println(e);
            }

        } else if (value == 5) {
            try {
                resultClassMonths = ReportsQuery.getMonthCount();
                colOne.setCellValueFactory(new PropertyValueFactory<>("nameOfMonth"));
                colTwo.setCellValueFactory(new PropertyValueFactory<>("monthCount"));
                colOne.setText("Month");
                colTwo.setText("Appointment in Month");
                resultsTable.setItems(resultClassMonths);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void returnToMain(ActionEvent actionEvent) throws IOException {
        this.stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        this.scene = FXMLLoader.load(this.getClass().getResource("/view/main.fxml"));
        this.stage.setScene(new Scene(this.scene));
        this.stage.show();
    }
}
