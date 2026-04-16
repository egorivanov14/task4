package com.innowise.web.service;

public interface UserService {
    boolean register(String username, String password);

    boolean login(String username, String password);
}