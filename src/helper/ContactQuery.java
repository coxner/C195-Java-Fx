package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ContactQuery {
    public static ObservableList<Contact> getContactFromDB() throws SQLException {
        String sql = "SELECT * FROM CONTACTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        while (rs.next()) {
            Contact contact = new Contact();
            contact.setContactId(rs.getInt("Contact_ID"));
            contact.setContactName(rs.getString("Contact_Name"));
            contact.setEmail(rs.getString("Email"));
            contactList.add(contact);
        }
        return contactList;
    }
}
