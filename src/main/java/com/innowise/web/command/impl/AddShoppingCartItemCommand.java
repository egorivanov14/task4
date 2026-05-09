package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.ShoppingCartServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.innowise.web.config.PublicConstants.*;

public class AddShoppingCartItemCommand implements Command { // todo logs
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = user.getId();
    String goodIdString = request.getParameter(GOOD_ID_PARAMETER);
    Long goodId = Long.parseLong(goodIdString);
    try {
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      if (!shoppingCartService.addItem(userId, goodId)) {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to addItem good to shopping cart");
      }
      GetAvailableGoodDtoListCommand getGoodDtoListCommand = new GetAvailableGoodDtoListCommand();
      return getGoodDtoListCommand.execute(request);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}
