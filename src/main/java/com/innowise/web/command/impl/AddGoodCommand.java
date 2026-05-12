package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class AddGoodCommand implements Command {
  private static final Logger logger = LogManager.getLogger(AddGoodCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing AddGoodCommand");
    String name = request.getParameter(NAME_PARAMETER);
    String priceStr = request.getParameter(PRICE_PARAMETER);
    Long price = Long.parseLong(priceStr);
    String quantityStr = request.getParameter(QUANTITY_PARAMETER);
    Long quantity = Long.parseLong(quantityStr);
    String manufacturer = request.getParameter(MANUFACTURER_PARAMETER);
    String description = request.getParameter(DESCRIPTION_PARAMETER);
    HttpSession session = request.getSession();
    UserDto userDto = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = userDto.getId();
    Good good = new Good(null, name, price, quantity, manufacturer, description, userId);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    try {
      if (goodService.add(good)) {
        logger.info("Good '{}' successfully added by user ID: {}", name, userId);
        session.setAttribute(CURRENT_PAGE_PARAMETER, ADD_GOOD_PAGE);
        request.setAttribute(INFO_MESSAGE_PARAMETER, "good added");
      } else {
        logger.warn("Failed to add good '{}' for user ID: {}", name, userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to add good");
      }
    } catch (ServiceException e) {
      logger.error("Service error during good addition", e);
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(ADD_GOOD_PAGE);
    return router;
  }
}