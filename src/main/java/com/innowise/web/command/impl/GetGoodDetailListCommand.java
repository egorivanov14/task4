package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.GoodDetailDto;
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

public class GetGoodDetailListCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetGoodDetailListCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing GetGoodListCommand");
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, GOOD_DETAIL_LIST_PAGE);
    List<GoodDetailDto> goodDetailDtoList;
    try {
      UserDto userDto = (UserDto) session.getAttribute(USER_PARAMETER);
      Long userId = userDto.getId();
      GoodServiceImpl goodService = GoodServiceImpl.getInstance();
      goodDetailDtoList = goodService.getGoodDtoListWithUsername(userId);
      request.setAttribute(GOOD_DETAIL_DTO_LIST_PARAMETER, goodDetailDtoList);
      logger.debug("Successfully retrieved {} goods for global list", goodDetailDtoList.size());
    } catch (ServiceException e) {
      logger.error("Failed to fetch global goods list", e);
      throw new CommandException(e);
    }
    return Router.forwardTo(GOOD_DETAIL_LIST_PAGE);
  }
}