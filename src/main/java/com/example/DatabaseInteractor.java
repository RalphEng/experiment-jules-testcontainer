package com.example;

import javax.sql.DataSource;
import java.sql.*;

public class DatabaseInteractor {

    private final DataSource dataSource;

    public DatabaseInteractor(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createUserTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(255))");
        }
    }

    public void addUser(int id, String name) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (id, name) VALUES (?, ?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        }
    }

    public String getUser(int id) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM users WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                } else {
                    return null;
                }
            }
        }
    }
}
