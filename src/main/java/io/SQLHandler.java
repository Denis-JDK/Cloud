package io;

import java.sql.*;

public class SQLHandler {

    private static Connection connection;
    private static Statement statement;

    public static void connect () {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:target/database.db");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disconnect () {

            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

    }

    public static String getNickByLoginAndPassword (String login, String password) {
        try {
          ResultSet rs = statement.executeQuery("SELECT nickname FROM users WHERE login = '" + login + "' AND password = '" + password + "'" );
          if (rs.next()) {
              return rs.getString("nickname");
          }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
