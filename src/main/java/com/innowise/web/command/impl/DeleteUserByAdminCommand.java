package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
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
    logger.debug("Executing DeleteUserByAdminCommand");
    String idString = request.getParameter(USER_ID_PARAMETER);
    if (idString == null || idString.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "user id is required");
      return Router.forwardTo(USER_LIST_PAGE);
    }

    Long userId;
    try {
      userId = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "user id must be a number");
      return Router.forwardTo(USER_LIST_PAGE);
    }
    try {
      logger.debug("Admin attempting to deleteById user ID: {}", userId);
      UserService userService = UserServiceImpl.getInstance();
      HttpSession session = request.getSession();
      UserDto currentUser = (UserDto) session.getAttribute(USER_PARAMETER);
      if (userService.deleteUserById(userId, currentUser)) {
        logger.info("Admin successfully deleted user ID: {}", userId);
        return Router.redirectTo(request.getContextPath() + GET_USER_LIST_COMMAND);
      } else {
        logger.warn("Admin failed to deleteById user ID: {}", userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to deleteById user, try again");
        return Router.forwardTo(USER_LIST_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("Service error during admin deletion of user ID: {}", userId, e);
      throw new CommandException(e);
    }
  }
}