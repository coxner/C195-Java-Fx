package model;

public class ResultSetThree {
    private String countryName;
    private int countForCountry;

    public ResultSetThree() {

    }


    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getCountForCountry() {
        return countForCountry;
    }

    public void setCountForCountry(int countForCountry) {
        this.countForCountry = countForCountry;
    }

    public ResultSetThree (String countryName, int countForCountry) {
        this.countryName = countryName;
        this.countForCountry = countForCountry;
    }

}
