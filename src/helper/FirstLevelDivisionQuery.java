package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class FirstLevelDivisionQuery {
    /**
     * Get all first level divisions from the database
     * @return get all first level division from the database
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivision> getFirstLevelDivisionFromDB() throws SQLException {
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<FirstLevelDivision> firstLvlDivList = FXCollections.observableArrayList();
        while (rs.next()) {
            FirstLevelDivision firstLvlDiv = new FirstLevelDivision();
            firstLvlDiv.setDivId(rs.getInt("Division_ID"));
            firstLvlDiv.setDiv(rs.getString("Division"));
            firstLvlDiv.setCountryId(rs.getInt("Country_ID"));
            firstLvlDivList.add(firstLvlDiv);
        }
        return firstLvlDivList;
    }
}
