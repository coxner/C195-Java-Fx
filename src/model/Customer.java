package model;

import helper.CustomerQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionId;
    public Customer(int id, String name, String address, String postalCode, String phoneNumber, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
    }

    public Customer() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public static ObservableList<Customer> allCustomers;

    static {
        try {
            allCustomers = CustomerQuery.getCustomerFromDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
    public static void addCustomer(Customer customer){allCustomers.add(customer);}
    public static boolean deleteCustomer(Customer customerToDelete) {
        if(allCustomers.contains(customerToDelete)) {
            allCustomers.remove(customerToDelete);
            return true;
        } else {
            return false;
        }

    }

    public String toString() {
        return Integer.toString(id);
    }



}
