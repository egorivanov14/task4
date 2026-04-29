package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.innowise.web.config.PublicConstants.*;

public class GetUsersListCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetUsersListCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.info("GetUsersListCommand executing");
    HttpSession session = request.getSession(false);
    session.setAttribute(CURRENT_PAGE_PARAMETER, USERS_PAGE);
    UserServiceImpl userService = UserServiceImpl.getInstance();
    try {
      List<UserDto> userDtoList = userService.getUserDtoList();//todo to ask if admin role checking is required
      request.setAttribute(USERS_PARAMETER, userDtoList);
    } catch (ServiceException e) {
      logger.error(e.getMessage());
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setPage(USERS_PAGE);
    router.setForward();
    return router;
  }
}