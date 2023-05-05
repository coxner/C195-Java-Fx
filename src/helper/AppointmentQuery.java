package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class AppointmentQuery {
    /**
     * Gets all the appointments from the database
     * @return a list of all appointments in database
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentFromDB() throws SQLException {
        String sql = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<Appointment> apptList = FXCollections.observableArrayList();
        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setId(rs.getInt("Appointment_ID"));
            appt.setTitle(rs.getString("Title"));
            appt.setDescription(rs.getString("Description"));
            appt.setLocation(rs.getString("Location"));
            appt.setType(rs.getString("Type"));
            appt.setStartDate(rs.getTimestamp("Start").toLocalDateTime());
            appt.setEndDate(rs.getTimestamp("End").toLocalDateTime());
            appt.setCustomerId(rs.getInt("Customer_ID"));
            appt.setUserId(rs.getInt("User_ID"));
            appt.setContactId(rs.getInt("Contact_ID"));
            apptList.add(appt);
        }
        return apptList;
    }

    /**
     * Insert a appointment into the database
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDate
     * @param endDate
     * @param customerId
     * @param userId
     * @param contactId
     * @return rows affected
     * @throws SQLException
     */
    public static int insert( String title, String description, String location, String type, LocalDateTime startDate, LocalDateTime endDate,
                             int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(startDate));
        ps.setTimestamp(6, Timestamp.valueOf(endDate));
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Delete an appointment from the database
     * @param id
     * @return rows affected
     * @throws SQLException
     */
    public static int deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffect = ps.executeUpdate();
        return rowsAffect;
    }

    /**
     * Updates a appointment in the database
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDate
     * @param endDate
     * @param customerId
     * @param userId
     * @param contactId
     * @return rows affected
     * @throws SQLException
     */
    public static int update(int id, String title, String description, String location, String type, LocalDateTime startDate, LocalDateTime endDate,
                             int customerId, int userId, int contactId) throws SQLException {
        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_id = ?" +
                " WHERE Appointment_ID = ? ";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, Timestamp.valueOf(startDate));
        ps.setTimestamp(6, Timestamp.valueOf(endDate));
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
