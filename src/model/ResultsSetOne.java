package model;

public class ResultsSetOne {
    private int resultCount;
    private String type;

    public ResultsSetOne() {

    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResultsSetOne(int resultCount, String type) {
        this.resultCount = resultCount;
        this.type = type;
    }
}
