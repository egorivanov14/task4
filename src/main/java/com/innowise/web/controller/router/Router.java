package com.innowise.web.controller.router;

public class Router {
  String page;
  Action action;

  public Router() {
  }

  public Router(String page, Action action) {
    this.page = page;
    this.action = action;
  }

  public static Router forwardTo(String page) {
    Router router = new Router();
    router.setForward();
    router.page = page;
    return router;
  }

  public static Router redirectTo(String page) {
    Router router = new Router();
    router.setRedirect();
    router.page = page;
    return router;
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