package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.CURRENT_PAGE_PARAMETER;
import static com.innowise.web.config.PublicConstants.PROFILE_PAGE;

public class GetProfileCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetProfileCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.info("GetProfileCommand executing");
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER,request.getContextPath() + PROFILE_PAGE);
    Router router = new Router();
    router.setPage(PROFILE_PAGE);
    router.setForward();
    return router;
  }
}