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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.innowise.web.config.PublicConstants.*;

public class GetShoppingCartByUserCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetShoppingCartByUserCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing GetShoppingCartByUserCommand");
    try {
      HttpSession session = request.getSession();
      UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
      Long userId = user.getId();
      logger.debug("Fetching cart for user ID: {}", userId);
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      List<ShoppingCartItemDto> shoppingCartItemDtoList = shoppingCartService.findAllDtoByUserId(userId);
      request.setAttribute(SHOPPING_CART_ITEM_DTO_LIST_PARAMETER, shoppingCartItemDtoList);
      logger.debug("Retrieved {} cart items for user ID: {}", shoppingCartItemDtoList.size(), userId);
      Router router = new Router();
      router.setForward();
      router.setPage(SHOPPING_CART_PAGE);
      logger.debug("Forwarding to page: {}", SHOPPING_CART_PAGE);
      return router;
    } catch (ServiceException e) {
      logger.error("ServiceException while fetching cart for user", e);
      throw new CommandException(e);
    }
  }
}