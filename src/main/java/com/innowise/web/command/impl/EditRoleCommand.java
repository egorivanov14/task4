package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;
import static com.innowise.web.service.impl.UserServiceImpl.getInstance;

public class EditRoleCommand implements Command {
  private static final Logger logger = LogManager.getLogger(EditRoleCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing EditRoleCommand");
    String toUserName = request.getParameter(TO_USERNAME_PARAMETER);
    String role = request.getParameter(ROLE_PARAMETER);

    if (toUserName == null || toUserName.isBlank() ||
            role == null || role.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "username and role are required");
      return Router.forwardTo(EDIT_ROLE_PAGE);
    }
    UserService userService = getInstance();
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, EDIT_ROLE_PAGE);
    UserDto currentUser = (UserDto) session.getAttribute(USER_PARAMETER);
    logger.info("Role change attempt by admin '{}' -> user '{}', target role: {}",
            currentUser.getUsername(), toUserName, role);
    try {
      if (userService.setRole(currentUser, toUserName, role)) {
        return Router.redirectTo(request.getContextPath() + GO_TO_EDIT_ROLE_COMMAND);
      } else {
        logger.warn("Role assignment failed or denied for user: {}", toUserName);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "Failed to change role");
        return Router.forwardTo(EDIT_ROLE_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("Service error during role edit for user: {}", toUserName, e);
      throw new CommandException(e);
    }
  }
}