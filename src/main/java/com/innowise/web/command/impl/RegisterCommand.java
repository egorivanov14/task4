package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.UserService;
import com.innowise.web.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class RegisterCommand implements Command {
  private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    String username = request.getParameter(USERNAME_PARAMETER);
    String password = request.getParameter(PASSWORD_PARAMETER);

    if (username == null || username.isBlank() ||
            password == null || password.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "username and password are required");
      return Router.forwardTo(REGISTER_PAGE);
    }
    logger.debug("Executing RegisterCommand for user: {}", username);
    Router router;
    try {
      UserService userService = UserServiceImpl.getInstance();
      if (userService.register(username, password)) {
        logger.info("Successfully registered user: {}", username);
        SetUserToSessionCommand setUserToSessionCommand = new SetUserToSessionCommand();
        router = setUserToSessionCommand.execute(request);
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_PAGE_PARAMETER, MAIN_PAGE);
      } else {
        logger.warn("Registration failed for user: {} (duplicate or invalid input)", username);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "user with this username already exists or exception on the server side");
        router = Router.forwardTo(REGISTER_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("Service error during registration of user: '{}'", username, e);
      throw new CommandException(e);
    }
    return router;
  }
}