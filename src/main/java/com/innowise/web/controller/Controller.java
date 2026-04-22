package com.innowise.web.controller;

import com.innowise.web.command.Command;
import com.innowise.web.command.CommandType;
import com.innowise.web.controller.router.Action;
import com.innowise.web.controller.router.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet(name = "controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {
  private static final Logger logger = LogManager.getLogger(Controller.class);

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    logger.info("Controller: doGet.");
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.info("Controller: doPost.");
    processRequest(request, response);
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    response.setContentType("text/html");

    String commandStr = request.getParameter("command");
    Command command = CommandType.defineCommand(commandStr);

    try {
      Router router = command.execute(request);
      Action action = router.getAction();
      String page = router.getPage();
      switch (action) {
        case REDIRECT:
          response.sendRedirect(page);
          break;
        case FORWARD:
          request.getRequestDispatcher(page).forward(request, response);
          break;
      }
    } catch (CommandException e) {
      response.sendError(500, e.getMessage());
    }
  }
}