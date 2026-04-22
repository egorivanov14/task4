package com.innowise.web.controller.router;

public class Router {
  String page;
  Action action;

  public Router() {}

  public Router(String page, Action action) {
    this.page = page;
    this.action = action;
  }

  public String getPage() {
    return page;
  }

  public Action getAction() {
    return action;
  }

  public void setRedirect() {
    this.action = Action.REDIRECT;
  }

  public void setForward() {
    this.action = Action.FORWARD;
  }

  public void setPage(String page) {
    this.page = page;
  }
}