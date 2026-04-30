package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.innowise.web.config.PublicConstants.*;

public class AddGoodCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    HttpSession session = request.getSession();
    session.setAttribute(CURRENT_PAGE_PARAMETER, ADD_GOOD_PAGE);
    String name = request.getParameter(NAME_PARAMETER);
    String priceStr = request.getParameter(PRICE_PARAMETER);
    Long price = Long.parseLong(priceStr);
    String quantityStr = request.getParameter(QUANTITY_PARAMETER);
    Long quantity = Long.parseLong(quantityStr);
    String manufacturer = request.getParameter(MANUFACTURER_PARAMETER);
    String description = request.getParameter(DESCRIPTION_PARAMETER);
    Good good = new Good(null, name, price, quantity, manufacturer, description);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    try {
      if (goodService.add(good)) {
        request.setAttribute(MESSAGE_PARAMETER, "good added");
      } else {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to add good");
      }
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(ADD_GOOD_PAGE);
    return router;
  }
}