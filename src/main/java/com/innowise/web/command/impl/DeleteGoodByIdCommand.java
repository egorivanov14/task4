package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class DeleteGoodByIdCommand implements Command {
  private static final Logger logger = LogManager.getLogger(DeleteGoodByIdCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing DeleteGoodByIdCommand");
    String idStr = request.getParameter(GOOD_ID_PARAMETER);
    Long goodId = Long.parseLong(idStr);
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    Router router = new Router();
    try {
      logger.debug("Attempting to delete good ID: {}", goodId);
      if (goodService.deleteById(goodId)) {
        logger.info("Good ID: {} successfully deleted", goodId);
        GetGoodDtoListByUserCommand getGoodDtoListByUserCommand = new GetGoodDtoListByUserCommand();
        router = getGoodDtoListByUserCommand.execute(request);
      } else {
        logger.warn("Good ID: {} deletion failed", goodId);
        request.setAttribute(ERROR_MESSAGE_PARAMETER, "failed to delete good, try again");
        router.setForward();
        router.setPage(GOOD_LIST_BY_USER_PAGE);
      }
    } catch (ServiceException e) {
      logger.error("Service error while deleting good ID: {}", goodId, e);
      throw new CommandException(e);
    }
    return router;
  }
}