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

    public static ObservableList<FirstLevelDivision> allFirstLvlDiv;

    static {
        try {
            allFirstLvlDiv = FirstLevelDivisionQuery.getFirstLevelDivisionFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObservableList<FirstLevelDivision> getAllFirstLvlDiv() {
        return allFirstLvlDiv;
    }

    public static FirstLevelDivision getFirstLevelById(int id) {
        for (FirstLevelDivision f : allFirstLvlDiv) {
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
