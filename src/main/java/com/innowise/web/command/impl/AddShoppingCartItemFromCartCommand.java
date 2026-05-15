package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.ShoppingCartServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class AddShoppingCartItemFromCartCommand implements Command {
  private static final Logger logger = LogManager.getLogger(AddShoppingCartItemFromCartCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing AddShoppingCartItemFromCartCommand");
    String goodIdString = request.getParameter(GOOD_ID_PARAMETER);
    if (goodIdString == null || goodIdString.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id is required");
      return Router.forwardTo(SHOPPING_CART_PAGE);
    }

    Long goodId;
    try {
      goodId = Long.parseLong(goodIdString);
    } catch (NumberFormatException e) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id must be a number");
      return Router.forwardTo(SHOPPING_CART_PAGE);
    }
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = user.getId();
    logger.debug("Adding good ID: {} to cart for user ID: {}", goodId, userId);
    try {
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      if (shoppingCartService.add(userId, goodId)) {
        return Router.redirectTo(request.getContextPath() + GET_SHOPPING_CART_BY_USER_COMMAND);
      } else {
        logger.warn("Failed to add good ID: {} to shopping cart for user ID: {}", goodId, userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to add good to shopping cart");
        return Router.forwardTo(SHOPPING_CART_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("ServiceException while adding good ID: {} to cart for user ID: {}", goodId, userId, e);
      throw new CommandException(e);
    }
  }
}