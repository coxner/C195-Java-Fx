package model;

import java.time.LocalDateTime;

public class ResultSetTwo {
    private int apptId;
    private String title;
    private String type;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int customerId;

    public ResultSetTwo() {

    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public ResultSetTwo(int apptId, String title, String type, String description, LocalDateTime startDate, LocalDateTime endDate,
                        int customerId) {
        this.apptId = apptId;
        this.title = title;
        this.type = type;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerId = customerId;
    }

}
