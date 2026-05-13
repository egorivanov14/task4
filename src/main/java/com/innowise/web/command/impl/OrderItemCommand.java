package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.ShoppingCartServiceImpl;
import com.innowise.web.service.impl.UserBalanceServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class OrderItemCommand implements Command {
  private static final Logger logger = LogManager.getLogger(OrderItemCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException { // todo logs
    String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
    String amountStr = request.getParameter(AMOUNT_PARAMETER);

    if (goodIdStr == null || goodIdStr.isBlank() ||
            amountStr == null || amountStr.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id and amount are required");
      return Router.forwardTo(SHOPPING_CART_PAGE);
    }

    Long goodId;
    Long amount;
    try {
      goodId = Long.valueOf(goodIdStr);
      amount = Long.valueOf(amountStr);
    } catch (NumberFormatException e) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id and amount must be numbers");
      return Router.forwardTo(SHOPPING_CART_PAGE);
    }
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      Long userId = user.getId();
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      if (shoppingCartService.order(userId, goodId, amount)) {
        request.setAttribute(MESSAGE_PARAMETER, "successful order");
        UserBalanceServiceImpl userBalanceService = UserBalanceServiceImpl.getInstance();
        Long balance = userBalanceService.getBalanceByUserId(userId);
        session.setAttribute(BALANCE_PARAMETER, balance);
      } else {
        request.setAttribute(MESSAGE_PARAMETER, "failed to order");
      }
      GetShoppingCartByUserCommand getShoppingCartByUserCommand = new GetShoppingCartByUserCommand();
      return getShoppingCartByUserCommand.execute(request);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}