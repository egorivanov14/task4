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
    logger.info("RegisterCommand executing.");
    UserService userService = UserServiceImpl.getInstance();
    String username = request.getParameter(USERNAME_PARAMETER);
    String password = request.getParameter(PASSWORD_PARAMETER);
    Router router = new Router();
    try {
      if (userService.register(username, password)) {
        logger.info("RegisterCommand executed successfully.");
        SetUserToSessionCommand setUserToSessionCommand = new SetUserToSessionCommand();
        router = setUserToSessionCommand.execute(request);
        HttpSession session = request.getSession();
        session.setAttribute(CURRENT_PAGE_PARAMETER, request.getContextPath() + MAIN_PAGE);
      } else {
        logger.info("RegisterCommand failed.");
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "user with this username already exists or you wrote null values");
        router.setPage(REGISTER_PAGE);
        router.setForward();
      }
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    return router;
  }
}