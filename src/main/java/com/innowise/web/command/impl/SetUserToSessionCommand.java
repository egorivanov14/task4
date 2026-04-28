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

import java.util.Optional;

import static com.innowise.web.config.PublicConstants.*;

public class SetUserToSessionCommand implements Command {
  private static final Logger logger = LogManager.getLogger(SetUserToSessionCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.info("SetUserToSessionCommand executing");
    UserServiceImpl userService = UserServiceImpl.getInstance();
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, request.getContextPath() + LOGIN_PAGE);
    String username = request.getParameter(USERNAME_PARAMETER);
    try {
      Optional<UserDto> optionalUserDto = userService.getUserDto(username);
      if (optionalUserDto.isPresent()) {
        logger.info("User set in session");
        UserDto userDto = optionalUserDto.get();
        session.setAttribute(USER_PARAMETER, userDto);
      } else {
        throw new CommandException("User not found");
      }
    } catch (ServiceException e) {
      logger.error(e.getMessage());
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setRedirect();
    router.setPage(request.getContextPath() + MAIN_PAGE);
    return router;
  }
}