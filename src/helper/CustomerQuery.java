package helper;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CustomerQuery {
    /**
     * Get all customer from the database
     * @return all customers from database
     * @throws SQLException
     */
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

    /**
     * Delete a customer from the database
     * @param customerId
     * @return rows affected
     * @throws SQLException
     */
    public static int deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        int rowsAffect = ps.executeUpdate();
        return rowsAffect;
    }

    /**
     * Insert a customer into the database
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param divisionID
     * @return rows affected
     * @throws SQLException
     */
    public static int insert(String customerName, String address, String postalCode, String phone, int divisionID) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Update customer in the database
     * @param customerID
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param divisionID
     * @return rows affected
     * @throws SQLException
     */
    public static int update(int customerID, String customerName, String address, String postalCode, String phone, int divisionID) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionID);
        ps.setInt(6, customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
