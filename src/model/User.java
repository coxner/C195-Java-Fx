package model;

import helper.UserQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class User {
    private int userId;
    private String userName;
    private String password;
    static ObservableList<User> allUsers;


    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    public User(int userId, String userName, String password) throws SQLException {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public User() throws SQLException {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Verify that the username and password user enters are valid
     * @param userName
     * @param password
     * @return boolean
     */
    public static boolean verifyUser(String userName, String password) throws SQLException {
        for (User user : UserQuery.getUserFromDB()) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(userId);
    }

}
