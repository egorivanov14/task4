package com.innowise.web.controller;

import com.innowise.web.command.Command;
import com.innowise.web.command.CommandType;
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
        response.setContentType("text/html");

        String commandStr = request.getParameter("command");
        Command command = CommandType.defineCommand(commandStr);

        String page = command.execute(request);

        request.getRequestDispatcher(page).forward(request, response);
    }
}