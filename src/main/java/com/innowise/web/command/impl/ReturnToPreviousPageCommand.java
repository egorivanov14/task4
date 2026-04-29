package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.innowise.web.config.PublicConstants.CURRENT_PAGE_PARAMETER;
import static com.innowise.web.config.PublicConstants.MAIN_PAGE;

public class ReturnToPreviousPageCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    HttpSession session = request.getSession();
    Object page = session.getAttribute(CURRENT_PAGE_PARAMETER);
    String pageStr = page.toString();
    String contextPath = request.getContextPath();
    Router router = new Router();
    if (pageStr != null && !pageStr.isBlank()) {
      router.setPage(contextPath + page);
    } else {
      router.setPage(contextPath + MAIN_PAGE);
    }
    router.setRedirect();
    return router;
  }
}