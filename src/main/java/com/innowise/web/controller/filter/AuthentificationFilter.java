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

import static com.innowise.web.config.PublicConstants.LOGIN_PAGE;
import static com.innowise.web.config.PublicConstants.USER_PARAMETER;

@WebFilter(urlPatterns = {"/pages/admin/*", "/pages/users/*"})
public class AuthentificationFilter implements Filter {
  private static final Logger logger = LogManager.getLogger(AuthentificationFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    logger.info("AuthentificationFilter Called");
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpSession session = httpRequest.getSession(false);
    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    if (session != null && user != null) {
      chain.doFilter(request, response);
    } else {
      httpResponse.sendRedirect(httpRequest.getContextPath() + LOGIN_PAGE);
    }
  }
}