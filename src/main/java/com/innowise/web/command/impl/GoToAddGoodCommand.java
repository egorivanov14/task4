package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

import static com.innowise.web.config.PublicConstants.ADD_GOOD_PAGE;

public class GoToAddGoodCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    return Router.forwardTo(ADD_GOOD_PAGE);
  }
}