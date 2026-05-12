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
  public Router execute(HttpServletRequest request) throws CommandException {
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      Long userId = user.getId();
      String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
      Long goodId = Long.valueOf(goodIdStr);
      String amountStr = request.getParameter(AMOUNT_PARAMETER);
      Long amount = Long.valueOf(amountStr);
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
      throw new RuntimeException(e);
    }
  }
}