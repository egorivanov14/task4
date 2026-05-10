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
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = user.getId();
    String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
    Long goodId = Long.parseLong(goodIdStr);
    String newQuantityStr = request.getParameter(NEW_QUANTITY_PARAMETER);
    Long newQuantity = Long.parseLong(newQuantityStr);
    logger.debug("Changing quantity for good ID: {} to {} by user ID: {}", goodId, newQuantity, userId);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    try {
      if (!goodService.changeQuantity(userId, goodId, newQuantity)) {
        logger.warn("Failed to change quantity for good ID: {} by user ID: {}", goodId, userId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to change good quantity");
      }
      GetGoodDtoListByUserCommand command = new GetGoodDtoListByUserCommand();
      logger.debug("Forwarding to GetGoodDtoListByUserCommand");
      return command.execute(request);
    } catch (ServiceException e) {
      logger.error("ServiceException while changing quantity for good ID: {} by user ID: {}", goodId, userId, e);
      throw new CommandException(e);
    }
  }
}