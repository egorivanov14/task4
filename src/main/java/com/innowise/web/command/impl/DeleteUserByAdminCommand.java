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
    logger.info("Entering DeleteUserByAdminCommand");
    UserService userService = UserServiceImpl.getInstance();
    String idString = request.getParameter(USER_ID_PARAMETER);
    Long id = Long.parseLong(idString);
    try {
      if (!userService.deleteUserById(id)) { //todo need some log to success deleting
        logger.warn("User {} was not deleted", id);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to delete user, try again");
      }
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    GetUserListCommand getUserListCommand = new GetUserListCommand();
    return getUserListCommand.execute(request);
  }
}