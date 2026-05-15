package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.UserDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.UserBalanceServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class UppBalanceCommand implements Command {
  private static final Logger logger = LogManager.getLogger(UppBalanceCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException { // todo logs
    String amountStr = request.getParameter(AMOUNT_PARAMETER);

    if (amountStr == null || amountStr.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "amount is required");
      return Router.forwardTo(UPP_BALANCE_PAGE);
    }

    Long amount;
    try {
      amount = Long.valueOf(amountStr);
    } catch (NumberFormatException e) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "amount must be a number");
      return Router.forwardTo(UPP_BALANCE_PAGE);
    }
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      Long userId = user.getId();
      UserBalanceServiceImpl userBalanceService = UserBalanceServiceImpl.getInstance();
      if (userBalanceService.uppBalance(userId, amount)) {
        Long balance = userBalanceService.getBalanceByUserId(userId);
        session.setAttribute(BALANCE_PARAMETER, balance);
        return Router.redirectTo(request.getContextPath() + GO_TO_UPP_BALANCE_COMMAND);
      } else {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to upp balance");
        return Router.forwardTo(UPP_BALANCE_PAGE);
      }
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}