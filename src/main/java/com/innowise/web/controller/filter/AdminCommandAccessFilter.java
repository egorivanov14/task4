package com.innowise.web.controller.filter;

import com.innowise.web.dto.UserDto;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import static com.innowise.web.config.PublicConstants.*;

@WebFilter(urlPatterns = "/controller")
public class AdminCommandAccessFilter implements Filter {

  @Override //todo logs
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpSession session = httpRequest.getSession(false);
    String command = httpRequest.getParameter(COMMAND_PARAMETER);

    if (!ADMIN_COMMAND_LIST.contains(command)) {
      chain.doFilter(request, response);
      return;
    }

    UserDto user = (UserDto) session.getAttribute(USER_PARAMETER);
    if (user != null && user.isAdmin()) {
      chain.doFilter(request, response);
    } else {
      httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }
}