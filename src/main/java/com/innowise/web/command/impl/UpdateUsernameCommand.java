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

public class UpdateUsernameCommand implements Command {
  private static final Logger logger = LogManager.getLogger(UpdateUsernameCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    HttpSession session = request.getSession();
    UserDto userDto = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = userDto.getId();
    String newUsername = request.getParameter(USERNAME_PARAMETER);
    UserService userService = UserServiceImpl.getInstance();
    try{
      if(userService.updateUsername(userId, newUsername)){
        logger.info("Username updated successfully {}", newUsername);
        userDto.setUsername(newUsername);
        session.setAttribute(USER_PARAMETER, userDto);
      }else {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to update username");
      }
    }catch (ServiceException e){
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(PROFILE_PAGE);
    return router;
  }
}