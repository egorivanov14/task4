package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

import static com.innowise.web.config.PublicConstants.GOOD_DETAIL_LIST_PAGE;
import static com.innowise.web.config.PublicConstants.GOOD_ID_PARAMETER;

public class DeleteGoodByAdminCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException { //todo logs, authorisation by filter
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
    Long goodId = Long.parseLong(goodIdStr);
    Router router = new Router();
    try {
      if (goodService.deleteById(goodId)) {
        GetGoodDetailListCommand command = new GetGoodDetailListCommand();
        router = command.execute(request);
      } else {
        router.setForward();
        router.setPage(GOOD_DETAIL_LIST_PAGE);
      }
      return router;
    } catch (ServiceException e) {
      throw new RuntimeException(e);
    }
  }
}