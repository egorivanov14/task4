package com.innowise.web.entity;

public class User {
    private String username;
    private String password;

    public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(obj.getClass() == this.getClass()){
            User objUser = (User) obj;
            return username.equals(objUser.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash += (username != null) ? username.hashCode() * 31 : 0;
        hash += (password != null) ? password.hashCode() : 0;
        return hash;
    }
}