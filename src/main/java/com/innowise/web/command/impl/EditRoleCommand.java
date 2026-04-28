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
    logger.info("SetRoleCommand executing");
    UserService userService = getInstance();
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, request.getContextPath() + EDIT_ROLE_PAGE);
    UserDto currentUser = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      String currentUserName = currentUser.getUsername();
      String toUserName = request.getParameter(TO_USERNAME_PARAMETER);
      String role = request.getParameter(ROLE_PARAMETER);
      if (!userService.setRole(currentUserName, toUserName, role)) {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "You dont have permission to perform this action.");
      }
    } catch (ServiceException e) {
      logger.error(e.getMessage());
      throw new CommandException(e);
    }
    logger.info("SetRoleCommand executed successfully");
    Router router = new Router();
    router.setPage(EDIT_ROLE_PAGE);
    router.setForward();
    return router;
  }
}