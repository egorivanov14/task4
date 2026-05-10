package com.innowise.web.command.impl;
import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.exception.CommandException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.GoodService;
import com.innowise.web.service.impl.GoodServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import static com.innowise.web.config.PublicConstants.GOOD_DTO_LIST_PARAMETER;
import static com.innowise.web.config.PublicConstants.GOOD_LIST_PAGE;
public class GetAvailableGoodDtoListCommand implements Command {
  private static final Logger logger = LogManager.getLogger(GetAvailableGoodDtoListCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException {
    logger.debug("Executing GetAvailableGoodDtoListCommand");
    GoodService goodService = GoodServiceImpl.getInstance();
    try {
      List<GoodDto> goodDtoList = goodService.getAvailableGoodDtoList();
      request.setAttribute(GOOD_DTO_LIST_PARAMETER, goodDtoList);
      logger.debug("Retrieved {} available goods", goodDtoList.size());
    } catch (ServiceException e) {
      logger.error("ServiceException while fetching available goods list", e);
      throw new CommandException(e);
    }
    Router router = new Router();
    router.setForward();
    router.setPage(GOOD_LIST_PAGE);
    logger.debug("Forwarding to page: {}", GOOD_LIST_PAGE);
    return router;
  }
}