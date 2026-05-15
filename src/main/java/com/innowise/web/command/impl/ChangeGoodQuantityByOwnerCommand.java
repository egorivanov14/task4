package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class ChangeGoodQuantityByOwnerCommand implements Command {
  private static final Logger logger = LogManager.getLogger(ChangeGoodQuantityByOwnerCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing ChangeGoodQuantityByOwnerCommand");
    String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
    String newQuantityStr = request.getParameter(NEW_QUANTITY_PARAMETER);

    if (goodIdStr == null || goodIdStr.isBlank() ||
            newQuantityStr == null || newQuantityStr.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id and quantity are required");
      return Router.forwardTo(GOOD_LIST_BY_USER_PAGE);
    }

    Long goodId;
    Long newQuantity;
    try {
      goodId = Long.parseLong(goodIdStr);
      newQuantity = Long.parseLong(newQuantityStr);
    } catch (NumberFormatException e) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id and quantity must be numbers");
      return Router.forwardTo(GOOD_LIST_BY_USER_PAGE);
    }
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = user.getId();
    logger.debug("Changing quantity for good ID: {} to {} by user ID: {}", goodId, newQuantity, userId);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    try {
      if (goodService.changeQuantity(userId, goodId, newQuantity)) {
        return Router.redirectTo(request.getContextPath() + GET_GOOD_DTO_LIST_BY_USER_COMMAND);
      } else {
        logger.warn("Failed to change quantity for good ID: {} by user ID: {}", goodId, userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to change good quantity");
        return Router.forwardTo(GOOD_LIST_BY_USER_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("ServiceException while changing quantity for good ID: {} by user ID: {}", goodId, userId, e);
      throw new CommandException(e);
    }
  }
}