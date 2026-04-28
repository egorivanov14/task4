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
    logger.info("Entering DeleteCurrentUserCommand");
    HttpSession session = request.getSession();
    UserDto userDto = (UserDto) session.getAttribute(USER_PARAMETER);
    logger.info("Deleting user {}", userDto.getUsername());
    UserService userService = UserServiceImpl.getInstance();
    Router router = new Router();
    String username = userDto.getUsername();
    try {
      if (userService.deleteUserByUsername(username)) {
        logger.info("User {} was successfully deleted", username);
        session.invalidate();
        router.setPage(request.getContextPath() + LOGIN_PAGE);
        router.setRedirect();
      } else {
        logger.warn("User {} was not deleted", username);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to delete user, try again");
        router.setPage(PROFILE_PAGE);
        router.setForward();
      }
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    return router;
  }
}