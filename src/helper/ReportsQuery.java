package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportsQuery {
    /**
     * Get appointment type results from the database
     * @return list of appointments by type
     * @throws SQLException
     */
    public static ObservableList<ResultsSetOne> getAppointmentTypeQuery() throws SQLException {
        String sql = "select count(*) as tApptByt, type from appointments group by type";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<ResultsSetOne> resultsList = FXCollections.observableArrayList();
        while (rs.next()) {
            ResultsSetOne result = new ResultsSetOne();
            result.setResultCount(rs.getInt("tApptByT"));
            result.setType(rs.getString("Type"));
            resultsList.add(result);
        }
        return resultsList;
    }

    /**
     * Get appointments for a specific contact
     * @param contactId
     * @return a list for the contact id passed
     * @throws SQLException
     */
    public static ObservableList<ResultSetTwo> getAppointmentForContact(int contactId) throws SQLException {
        String sql = "select Appointment_ID, Title, Type, Description, Start, End, Customer_ID from appointments join contacts on contacts.Contact_ID = appointments.Contact_ID where contacts.Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactId);
        ResultSet rs = ps.executeQuery();
        ObservableList<ResultSetTwo> resultsList = FXCollections.observableArrayList();
        while (rs.next()) {
            ResultSetTwo result = new ResultSetTwo();
            result.setApptId(rs.getInt("Appointment_ID"));
            result.setTitle(rs.getString("Title"));
            result.setType(rs.getString("Type"));
            result.setDescription(rs.getString("Description"));
            result.setStartDate(rs.getTimestamp("Start").toLocalDateTime());
            result.setEndDate(rs.getTimestamp("End").toLocalDateTime());
            result.setCustomerId(rs.getInt("Customer_ID"));
            resultsList.add(result);
        }
        return resultsList;
    }

    /**
     * Returns a list of countries with a count of provinces / states
     * @return list of countries with there # of states and provinces
     * @throws SQLException
     */
    public static ObservableList<ResultSetThree> getCountCountry() throws SQLException {
        String sql = "SELECT  countries.country, count(*) as count from first_level_divisions join countries on countries.Country_ID = first_level_divisions.Country_ID group by Country";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<ResultSetThree> resultsList = FXCollections.observableArrayList();
        while (rs.next()) {
            ResultSetThree result = new ResultSetThree();
            result.setCountryName(rs.getString("country"));
            result.setCountForCountry(rs.getInt("count"));
            resultsList.add(result);
        }
        return resultsList;
    }

    public static ObservableList<ResultClassMonth> getMonthCount() throws  SQLException {
        String sql = "SELECT monthname((start)) as 'Month', count(*) as count from appointments group by Month";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<ResultClassMonth> resultsList = FXCollections.observableArrayList();
        while (rs.next()) {
            ResultClassMonth result = new ResultClassMonth();
            result.setNameOfMonth(rs.getString("Month"));
            result.setMonthCount(rs.getInt("count"));
            resultsList.add(result);

        }
        return resultsList;

    }
}
