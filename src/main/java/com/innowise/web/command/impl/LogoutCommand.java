package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.LOGIN_PAGE;

public class LogoutCommand implements Command {
  private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

  @Override
  public Router execute(HttpServletRequest request) {
    logger.info("LogoutCommand execute.");
    request.getSession().invalidate();
    Router router = new Router();
    router.setPage(request.getContextPath() + LOGIN_PAGE);
    router.setRedirect();
    return router;
  }
}