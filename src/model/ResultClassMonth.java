package model;

public class ResultClassMonth {
    private String nameOfMonth;
    private int monthCount;

    public ResultClassMonth() {

    }

    public String getNameOfMonth() {
        return nameOfMonth;
    }

    public void setNameOfMonth(String nameOfMonth) {
        this.nameOfMonth = nameOfMonth;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    public ResultClassMonth(String nameOfMonth, int monthCount) {
        this.nameOfMonth = nameOfMonth;
        this.monthCount = monthCount;

    }
}
