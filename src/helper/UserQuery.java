package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UserQuery {
    /**
     * Gets all users from the database
     * @return all users from the database
     * @throws SQLException
     */
    public static ObservableList<User> getUserFromDB() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ObservableList<User> userList = FXCollections.observableArrayList();
        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("User_ID"));
            user.setUserName(rs.getString("User_Name"));
            user.setPassword(rs.getString("Password"));
            userList.add(user);
        }
        return userList;
    }
}
