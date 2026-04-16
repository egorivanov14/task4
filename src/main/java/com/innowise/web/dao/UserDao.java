package com.innowise.web.dao;

import com.innowise.web.entity.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}