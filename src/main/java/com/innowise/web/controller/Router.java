package com.innowise.web.controller;

public class Router {
    private String page;
    private Action action;

    public Router(String page, Action action) {
        this.page = page;
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setForward() {
        this.action = Action.FORWARD;
    }

    public void setRedirect() {
        this.action = Action.REDIRECT;
    }
}