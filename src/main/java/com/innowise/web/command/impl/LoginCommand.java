package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class LoginCommand implements Command {
  private static final Logger logger = LogManager.getLogger(LoginCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.info("LoginCommand executing.");
    String username = request.getParameter(USER_NAME_PARAMETER);
    String password = request.getParameter(PASSWORD_PARAMETER);
    UserServiceImpl userService = UserServiceImpl.getInstance();
    boolean authenticated;
    try {
      authenticated = userService.login(username, password);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    HttpSession session = request.getSession();
    Router router = new Router();
    if (authenticated) {
      logger.info("LoginCommand executed successfully.");
      session.setAttribute(USER_NAME_PARAMETER, username);
      router.setPage(MAIN_PAGE);
      router.setRedirect();
    } else {
      logger.info("LoginCommand failed.");
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to login");
      router.setPage(LOGIN_PAGE);
      router.setForward();
    }
    return router;
  }
}