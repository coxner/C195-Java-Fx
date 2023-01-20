package model;

import helper.CountryQuery;
import helper.FirstLevelDivisionQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Country {
    private int countryId;
    private String country;

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

    public Country() {}

    public static ObservableList<Country> allCountries;

    static {
        try {
            allCountries = CountryQuery.getCountryFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    private static ObservableList<FirstLevelDivision> associatedFLD = FXCollections.observableArrayList();


    public static Country getCountryById(FirstLevelDivision fld) {
        for (Country c : allCountries) {
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



