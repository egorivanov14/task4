package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.UserBalanceServiceImpl;
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
    UserServiceImpl userService = UserServiceImpl.getInstance();
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, LOGIN_PAGE);
    String username = request.getParameter(USERNAME_PARAMETER);
    logger.debug("Binding user '{}' to session", username);
    try {
      Optional<UserDto> optionalUserDto = userService.getUserDto(username);
      if (optionalUserDto.isPresent()) {
        UserDto userDto = optionalUserDto.get();
        session.setAttribute(USER_PARAMETER, userDto);
        Long userId = userDto.getId();
        UserBalanceServiceImpl userBalanceService = UserBalanceServiceImpl.getInstance();
        Long balance = userBalanceService.getBalanceByUserId(userId);
        session.setAttribute(BALANCE_PARAMETER, balance);
        logger.debug("User '{}' successfully bound to session", userDto.getUsername());
      } else {
        logger.warn("User '{}' not found during session binding", username);
        throw new CommandException("User not found");
      }
    } catch (ServiceException e) {
      logger.error("Failed to fetch user data for session binding: '{}'", username, e);
      throw new CommandException(e);
    }
    return Router.redirectTo(request.getContextPath() + MAIN_PAGE);
  }
}