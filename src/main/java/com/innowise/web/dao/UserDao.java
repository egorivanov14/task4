package com.innowise.web.dao;

import com.innowise.web.entity.User;

import java.util.List;

public interface UserDao {
    boolean add(User user);

    boolean update(User user);

    boolean delete(User user);

    List<User> findAll();
}