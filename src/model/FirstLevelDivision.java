package model;

import helper.CustomerQuery;
import helper.FirstLevelDivisionQuery;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class FirstLevelDivision {
    private int divId;
    private String div;
    private int countryId;

    public FirstLevelDivision(int divId, String div, int countryId) {
        this.divId = divId;
        this.div = div;
        this.countryId = countryId;
    }

    public FirstLevelDivision() {

    }

    public int getDivId() {
        return divId;
    }

    public void setDivId(int div_id) {
        this.divId = div_id;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int country_id) {
        this.countryId = country_id;
    }





    /**
     * Get a first level division by its id
     * @param id
     * @return first level division matching id
     */
    public static FirstLevelDivision getFirstLevelById(int id) throws SQLException {
        for (FirstLevelDivision f : FirstLevelDivisionQuery.getFirstLevelDivisionFromDB()) {
            if (f.getDivId() == id)
                return f;
        }
        return null;
    }

    @Override
    public String toString() {
        return (div);
    }
}
