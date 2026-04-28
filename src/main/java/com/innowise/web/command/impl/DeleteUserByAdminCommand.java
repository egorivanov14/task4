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

public class DeleteUserByAdminCommand implements Command {
  private static final Logger logger = LogManager.getLogger(DeleteUserByAdminCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.info("Entering DeleteUserByAdminCommand");
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, request.getContextPath() + USERS_PAGE);
    UserService userService = UserServiceImpl.getInstance();
    String username = request.getParameter(USERNAME_PARAMETER);
    try {
      if (!userService.deleteUserByUsername(username)) {
        logger.warn("User {} was not deleted", username);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to delete user, try again");
      }
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setPage(USERS_PAGE);
    router.setRedirect();
    return router;
  }
}