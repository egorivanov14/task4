package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.innowise.web.config.PublicConstants.*;

public class GetGoodDtoListByUserCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetGoodDtoListByUserCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing GetGoodDtoListByUserCommand");
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = user.getId();
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    try {
      List<GoodDto> goodDtoList = goodService.getGoodDtoListByUserId(userId);
      request.setAttribute(GOOD_DTO_LIST_PARAMETER, goodDtoList);
    } catch (ServiceException e) {
      logger.error("Failed to fetch goods for user ID: {}", userId, e);
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to get goods");
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(GOOD_LIST_BY_USER_PAGE);
    return router;
  }
}