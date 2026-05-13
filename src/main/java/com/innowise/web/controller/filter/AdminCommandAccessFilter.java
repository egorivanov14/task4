package com.innowise.web.controller.filter;

import com.innowise.web.dto.UserDto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static com.innowise.web.config.PublicConstants.*;

public class AdminCommandAccessFilter implements Filter {
  private static final Logger logger = LogManager.getLogger(AdminCommandAccessFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    logger.debug("Filter invoked: URI={}, command={}", httpRequest.getRequestURI(), httpRequest.getParameter(COMMAND_PARAMETER));

    HttpSession session = httpRequest.getSession(false);
    String command = httpRequest.getParameter(COMMAND_PARAMETER);

    if (!ADMIN_COMMAND_LIST.contains(command)) {
      logger.debug("Command '{}' does not require admin rights, proceeding", command);
      chain.doFilter(request, response);
      return;
    }

    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    if (user.isAdmin()) {
      logger.debug("Admin access granted for command '{}' by user '{}'", command, user.getUsername());
      chain.doFilter(request, response);
    } else {
      logger.warn("Admin command '{}' access denied for user '{}' (role: {})", command, user.getUsername(), user.getRole());
      httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }
}