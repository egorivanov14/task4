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

public class DeleteGoodCommand implements Command {
  private static final Logger logger = LogManager.getLogger(DeleteGoodCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing DeleteGoodByIdCommand");
    String idStr = request.getParameter(GOOD_ID_PARAMETER);
    if (idStr == null || idStr.isBlank()) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id is required");
      return Router.forwardTo(GOOD_LIST_BY_USER_PAGE);
    }

    Long goodId;
    try {
      goodId = Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      request.setAttribute(ERROR_MESSAGE_PARAMETER, "good id must be a number");
      return Router.forwardTo(GOOD_LIST_BY_USER_PAGE);
    }
    Router router;
    try {
      logger.debug("Attempting to deleteById good ID: {}", goodId);
      HttpSession session = request.getSession();
      UserDto currentUser = (UserDto) session.getAttribute(USER_PARAMETER);
      GoodServiceImpl goodService = GoodServiceImpl.getInstance();
      if (goodService.deleteById(goodId, currentUser)) {
        logger.info("Good ID: {} successfully deleted", goodId);
        request.setAttribute(INFO_MESSAGE_PARAMETER, "good deleted successfully");
        GetGoodDtoListByUserCommand getGoodDtoListByUserCommand = new GetGoodDtoListByUserCommand();
        router = getGoodDtoListByUserCommand.execute(request);
      } else {
        logger.warn("Good ID: {} deletion failed", goodId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to deleteById good, try again");
        router = Router.forwardTo(GOOD_LIST_BY_USER_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("Service error while deleting good ID: {}", goodId, e);
      throw new CommandException(e);
    }
    return router;
  }
}