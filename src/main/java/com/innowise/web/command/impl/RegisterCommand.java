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
    UserService userService = UserServiceImpl.getInstance();
    String username = request.getParameter(USERNAME_PARAMETER);
    logger.debug("Executing RegisterCommand for user: {}", username);
    String password = request.getParameter(PASSWORD_PARAMETER);
    Router router = new Router();
    try {
      if (userService.register(username, password)) {
        logger.info("Successfully registered user: {}", username);
        SetUserToSessionCommand setUserToSessionCommand = new SetUserToSessionCommand();
        router = setUserToSessionCommand.execute(request);
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_PAGE_PARAMETER, MAIN_PAGE);
      } else {
        logger.warn("Registration failed for user: {} (duplicate or invalid input)", username);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "user with this username already exists or you wrote null values");
        router.setPage(REGISTER_PAGE);
        router.setForward();
      }
    } catch (ServiceException e) {
      logger.error("Service error during registration of user: '{}'", username, e);
      throw new CommandException(e);
    }
    return router;
  }
}