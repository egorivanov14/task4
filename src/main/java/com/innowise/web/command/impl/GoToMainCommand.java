package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import static com.innowise.web.config.PublicConstants.MAIN_PAGE;

public class GoToMainCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    return Router.forwardTo(MAIN_PAGE);
  }
}