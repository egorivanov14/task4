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
    HttpSession session = request.getSession();
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    try {
      Long userId = user.getId();
      String amountStr = request.getParameter(AMOUNT_PARAMETER);
      Long amount = Long.valueOf(amountStr);
      UserBalanceServiceImpl userBalanceService = UserBalanceServiceImpl.getInstance();
      if (userBalanceService.uppBalance(userId, amount)) {
        request.setAttribute(MESSAGE_PARAMETER, "balance updated");
        Long balance = userBalanceService.getBalanceByUserId(userId);
        session.setAttribute(BALANCE_PARAMETER, balance);
      } else {
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to upp balance");
      }
      Router router = new Router();
      router.setForward();
      router.setPage(UPP_BALANCE_PAGE);
      return router;
    } catch (ServiceException e) {
      throw new CommandException(e);
    }
  }
}