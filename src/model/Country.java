package model;

import helper.CountryQuery;
import helper.FirstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Country {
    private int countryId;
    private String country;

    public Country() {

    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Country(int country_Id, String country) {
        this.countryId = country_Id;
        this.country = country;
    }


    /**
     * Get a country by its id
     * @param fld
     * @return country matching id
     */
    public static Country getCountryById(FirstLevelDivision fld) throws SQLException {
        for (Country c : CountryQuery.getCountryFromDB()) {
            if (c.getCountryId() == fld.getCountryId()) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return (country);
    }

    }



