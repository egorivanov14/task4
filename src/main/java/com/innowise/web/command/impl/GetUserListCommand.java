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

public class GetUserListCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetUserListCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing GetUserListCommand");
    HttpSession session = request.getSession(false);
    session.setAttribute(CURRENT_PAGE_PARAMETER, USER_LIST_PAGE);
    UserServiceImpl userService = UserServiceImpl.getInstance();
    try {
      UserDto userDto = (UserDto) session.getAttribute(USER_PARAMETER);
      Long userId = userDto.getId();
      List<UserDto> userDtoList = userService.getUserDtoList(userId);
      request.setAttribute(USER_LIST_PARAMETER, userDtoList);
      logger.debug("Successfully retrieved {} users for admin list", userDtoList.size());
    } catch (ServiceException e) {
      logger.error("Failed to fetch user list", e);
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setPage(USER_LIST_PAGE);
    router.setForward();
    return router;
  }
}