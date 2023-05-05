package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CountryQuery {
    /**
     * Gets all the countries from the database
     * @return full list of contacts from database
     * @throws SQLException
     */
    public static ObservableList<Country> getCountryFromDB() throws SQLException {
        String sql = "SELECT * FROM COUNTRIES";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        while (rs.next()) {
            Country country = new Country();
            country.setCountryId(rs.getInt("Country_ID"));
            country.setCountry(rs.getString("Country"));
            countryList.add(country);
        }
        return countryList;
    }
}
