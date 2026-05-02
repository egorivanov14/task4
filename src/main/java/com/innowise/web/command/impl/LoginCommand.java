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
    String username = request.getParameter(USERNAME_PARAMETER);
    logger.debug("Executing LoginCommand for user: {}", username);
    String password = request.getParameter(PASSWORD_PARAMETER);
    UserServiceImpl userService = UserServiceImpl.getInstance();
    Router router = new Router();
    try {
      if (userService.login(username, password)) {
        logger.info("Successful authentication for user: {}", username);
        SetUserToSessionCommand setUserToSessionCommand = new SetUserToSessionCommand();
        router = setUserToSessionCommand.execute(request);
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_PAGE_PARAMETER, MAIN_PAGE);
      } else {
        logger.warn("Failed login attempt for user: {}", username);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to login");
        router.setPage(LOGIN_PAGE);
        router.setForward();
      }
    } catch (ServiceException e) {
      logger.error("Service error during login for user: '{}'", username, e);
      throw new CommandException(e);
    }
    return router;
  }
}