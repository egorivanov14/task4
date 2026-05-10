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

public class DeleteGoodByAdminCommand implements Command {
  private static final Logger logger = LogManager.getLogger(DeleteGoodByAdminCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing DeleteGoodByAdminCommand");
    GoodServiceImpl goodService = GoodServiceImpl.getInstance();
    String goodIdStr = request.getParameter(GOOD_ID_PARAMETER);
    Long goodId = Long.parseLong(goodIdStr);
    HttpSession session = request.getSession();
    UserDto currentUser = (UserDto) session.getAttribute(USER_PARAMETER);
    Router router = new Router();
    try {
      if (goodService.deleteById(goodId, currentUser)) {
        logger.info("Good ID: {} successfully deleted by user: {}", goodId, currentUser.getUsername());
        request.setAttribute(INFO_MESSAGE_PARAMETER, "good successfully deleted");
        GetGoodDetailListCommand command = new GetGoodDetailListCommand();
        router = command.execute(request);
      } else {
        logger.warn("Failed to delete good ID: {} by user: {}", goodId, currentUser.getUsername());
        router.setForward();
        router.setPage(GOOD_DETAIL_LIST_PAGE);
      }
      return router;
    } catch (ServiceException e) {
      logger.error("ServiceException while deleting good ID: {} by user: {}", goodId, currentUser.getUsername(), e);
      throw new RuntimeException(e);
    }
  }
}