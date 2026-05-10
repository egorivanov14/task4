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

public class RemoveItemFromShoppingCartCommand implements Command {
  private static final Logger logger = LogManager.getLogger(RemoveItemFromShoppingCartCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing RemoveItemFromShoppingCartCommand");
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      ShoppingCartServiceImpl shoppingCartService = ShoppingCartServiceImpl.getInstance();
      Long userId = user.getId();
      String goodIdString = request.getParameter(GOOD_ID_PARAMETER);
      Long goodId = Long.parseLong(goodIdString);
      logger.debug("Removing good ID: {} from cart of user ID: {}", goodId, userId);
      if (!shoppingCartService.removeItem(userId, goodId)) {
        logger.warn("Failed to remove good ID: {} from cart of user ID: {}", goodId, userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to remove");
      }
      GetShoppingCartByUserCommand command = new GetShoppingCartByUserCommand();
      logger.debug("Forwarding to GetShoppingCartByUserCommand");
      return command.execute(request);
    } catch (ServiceException e) {
      logger.error("ServiceException while removing item from cart", e);
      throw new CommandException(e);
    }
  }
}