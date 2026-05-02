package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.LOGIN_PAGE;
import static com.innowise.web.config.PublicConstants.USER_PARAMETER;

public class LogoutCommand implements Command {
  private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

  @Override
  public Router execute(HttpServletRequest request) {
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    String username = user.getUsername();
    logger.info("User '{}' logging out", username);
    request.getSession().invalidate();
    Router router = new Router();
    router.setPage(request.getContextPath() + LOGIN_PAGE);
    router.setRedirect();
    return router;
  }
}