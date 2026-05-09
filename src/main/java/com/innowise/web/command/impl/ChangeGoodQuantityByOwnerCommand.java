package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static com.innowise.web.config.PublicConstants.*;

public class ChangeGoodQuantityByOwnerCommand implements Command {
  @Override
  public Router execute(HttpServletRequest request) throws CommandException { // todo logs
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    Long userId = user.getId();
    String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
    Long goodId = Long.parseLong(goodIdStr);
    String newQuantityStr = request.getParameter(NEW_QUANTITY_PARAMETER);
    Long newQuantity = Long.parseLong(newQuantityStr);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    try {
      if (!goodService.changeQuantity(userId, goodId, newQuantity)) {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to change good quantity");
      }
      GetGoodDtoListByUserCommand command = new GetGoodDtoListByUserCommand();
      return command.execute(request);
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}