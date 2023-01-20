package helper;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerQuery {
    public static ObservableList<Customer> getCustomerFromDB() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Customer> custList = FXCollections.observableArrayList();
        while (rs.next()) {
            Customer cust = new Customer();
            cust.setId(rs.getInt("Customer_ID"));
            cust.setName(rs.getString("Customer_Name"));
            cust.setAddress(rs.getString("Address"));
            cust.setPostalCode(rs.getString("Postal_Code"));
            cust.setPhoneNumber(rs.getString("Phone"));
            cust.setDivisionId(rs.getInt("Division_ID"));
            custList.add(cust);
        }
        return custList;
    }
    public static int deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffect = ps.executeUpdate();
        return rowsAffect;
    }
    public static int insert(int customerID, String customerName, String address, String postalCode, String phone, int divisionID) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.setString(2, customerName);
        ps.setString(3, address);
        ps.setString(4, postalCode);
        ps.setString(5, phone);
        ps.setInt(6, divisionID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
