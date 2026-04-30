package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import static com.innowise.web.config.PublicConstants.*;

public class GetGoodListCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, GOOD_LIST_PAGE);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    List<Good> goodList;
    try {
      goodList = goodService.findAll();
      request.setAttribute(GOOD_LIST_PARAMETER, goodList);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(GOOD_LIST_PAGE);
    return router;
  }
}