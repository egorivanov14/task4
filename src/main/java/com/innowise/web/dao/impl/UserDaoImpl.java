package com.innowise.web.dao.impl;

import com.innowise.web.connection.DataBaseConnection;
import com.innowise.web.dao.UserDao;
import com.innowise.web.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String ADD_USER_SQL = "INSERT INTO users (user_name, password) VALUES (?, ?)";

    @Override
    public boolean add(User user) {
        DataBaseConnection dbc = new DataBaseConnection();
        Connection connection = dbc.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(ADD_USER_SQL);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return null;
    }
}