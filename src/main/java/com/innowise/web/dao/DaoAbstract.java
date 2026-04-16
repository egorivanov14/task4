package com.innowise.web.dao;

import com.innowise.web.entity.User;

import java.util.List;

public abstract class DaoAbstract<T> {
    public abstract boolean add(T t);

    public abstract boolean update(T t);

    public abstract boolean delete(T t);

    public abstract List<User> findAll();
}