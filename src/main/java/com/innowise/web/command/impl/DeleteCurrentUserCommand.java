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

public class DeleteCurrentUserCommand implements Command {
  private static final Logger logger = LogManager.getLogger(DeleteCurrentUserCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing DeleteCurrentUserCommand");
    HttpSession session = request.getSession();
    UserDto currentUser = (UserDto) session.getAttribute(USER_PARAMETER);
    UserService userService = UserServiceImpl.getInstance();
    Router router = new Router();
    Long userId = currentUser.getId();
    try {
      if (userService.deleteUserById(userId, currentUser)) {
        logger.info("User ID: {} successfully deleted (self-service)", userId);
        session.invalidate();
        router.setPage(request.getContextPath() + LOGIN_PAGE);
        router.setRedirect();
      } else {
        logger.warn("User ID: {} deletion failed at database layer", userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to deleteById user, try again");
        router.setPage(PROFILE_PAGE);
        router.setForward();
      }
    } catch (ServiceException e) {
      logger.error("Service error while deleting user ID: {}", userId, e);
      throw new CommandException(e);
    }
    return router;
  }
}