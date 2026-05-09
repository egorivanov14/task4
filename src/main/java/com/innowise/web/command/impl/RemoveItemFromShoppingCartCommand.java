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

public class RemoveItemFromShoppingCartCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException { // todo logs
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      Long userId = user.getId();
      String goodIdString = request.getParameter(GOOD_ID_PARAMETER);
      Long goodId = Long.parseLong(goodIdString);
      if (!shoppingCartService.removeItem(userId, goodId)) {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to remove");
      }
      GetShoppingCartByUserCommand command = new GetShoppingCartByUserCommand();
      return command.execute(request);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}