package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.ShoppingCartItemDto;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.ShoppingCartServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import static com.innowise.web.config.PublicConstants.*;

public class GetShoppingCartByUserCommand implements Command { // todo logs
  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    try{
      HttpSession session = request.getSession();
      UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
      Long userId = user.getId();
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      List<ShoppingCartItemDto> shoppingCartItemDtoList = shoppingCartService.findAllDtoByUserId(userId);
      request.setAttribute(SHOPPING_CART_ITEM_DTO_LIST_PARAMETER, shoppingCartItemDtoList);
      Router router = new Router();
      router.setForward();
      router.setPage(SHOPPING_CART_PAGE);
      return router;
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}