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

import static com.innowise.web.config.PublicConstants.MAIN_PAGE;
import static com.innowise.web.config.PublicConstants.USER_PARAMETER;

@WebFilter(urlPatterns = {"/pages/admin/*"})
public class AdminAccessFilter implements Filter {
  private static final Logger logger = LogManager.getLogger(AdminAccessFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    HttpSession httpSession = httpServletRequest.getSession(false);
    UserDto user = (UserDto) httpSession.getAttribute(USER_PARAMETER);
    if (user.isAdmin()) {
      logger.debug("Admin access granted for user: {}", user.getUsername());
      chain.doFilter(request, response);
    } else {
      logger.warn("Access denied for user: {}", user.getUsername());
      httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + MAIN_PAGE);
    }
  }
}