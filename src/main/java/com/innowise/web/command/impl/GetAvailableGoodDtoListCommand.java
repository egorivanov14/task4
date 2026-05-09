package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.GoodService;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.innowise.web.config.PublicConstants.GOOD_DTO_LIST_PARAMETER;
import static com.innowise.web.config.PublicConstants.GOOD_LIST_PAGE;

public class GetAvailableGoodDtoListCommand implements Command { // todo logs, current page
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    GoodService goodService = GoodServiceImpl.getInstance();
    try {
      List<GoodDto> goodDtoList = goodService.getAvailableGoodDtoList();
      request.setAttribute(GOOD_DTO_LIST_PARAMETER, goodDtoList);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(GOOD_LIST_PAGE);
    return router;
  }
}