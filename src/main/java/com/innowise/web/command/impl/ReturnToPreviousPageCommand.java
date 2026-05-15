package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.CURRENT_PAGE_PARAMETER;
import static com.innowise.web.config.PublicConstants.MAIN_PAGE;

public class ReturnToPreviousPageCommand implements Command {
  private static final Logger logger = LogManager.getLogger(ReturnToPreviousPageCommand.class);

  @Override
  public Router execute(HttpServletRequest request) throws CommandException { // todo
    logger.debug("Executing ReturnToPreviousPageCommand");
    HttpSession session = request.getSession();
    Object page = session.getAttribute(CURRENT_PAGE_PARAMETER);
    String pageStr = page.toString();
    String contextPath = request.getContextPath();
    Router router = new Router();
    if (pageStr != null && !pageStr.isBlank()) {
      logger.debug("Redirecting to previous page: {}", page);
      router.setPage(contextPath + page);
    } else {
      logger.debug("No previous page in session, falling back to main");
      router.setPage(contextPath + MAIN_PAGE);
    }
    router.setRedirect();
    return router;
  }
}