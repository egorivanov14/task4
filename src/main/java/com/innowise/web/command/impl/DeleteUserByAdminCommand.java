package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.UserService;
import com.innowise.web.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class DeleteUserByAdminCommand implements Command {
  private static final Logger logger = LogManager.getLogger(DeleteUserByAdminCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing DeleteUserByAdminCommand");
    UserService userService = UserServiceImpl.getInstance();
    String idString = request.getParameter(USER_ID_PARAMETER);
    Long userId = Long.parseLong(idString);
    Router router = new Router();
    try {
      logger.debug("Admin attempting to delete user ID: {}", userId);
      if (userService.deleteUserById(userId)) {
        logger.info("Admin successfully deleted user ID: {}", userId);
        request.setAttribute(MESSAGE_PARAMETER, "user deleted successfully");
        GetUserListCommand getUserListCommand = new GetUserListCommand();
        router = getUserListCommand.execute(request);
      } else {
        logger.warn("Admin failed to delete user ID: {}", userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to delete user, try again");
        router.setForward();
        router.setPage(USER_LIST_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("Service error during admin deletion of user ID: {}", userId, e);
      throw new CommandException(e);
    }
    return router;
  }
}