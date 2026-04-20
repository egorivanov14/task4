package com.innowise.web.controller;

import com.innowise.web.command.Command;
import com.innowise.web.command.CommandType;
import com.innowise.web.connection.ConnectionPool;
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

    @Override
    public void init() throws ServletException {
        ConnectionPool.getInstance();
        super.init();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("Controller: doGet.");
        response.setContentType("text/html");

        String commandStr = request.getParameter("command");
        Command command = CommandType.defineCommand(commandStr);

        try {
            Router router = command.execute(request);
            String page = router.getPage();
            Action action = router.getAction();
            switch (action) {
                case FORWARD:
                    request.getRequestDispatcher(page).forward(request, response);
                    return;
                case REDIRECT:
                    response.sendRedirect(page);
            }
        } catch (CommandException e) {
            response.sendError(500);
        }
    }

    @Override
    public void destroy() {
        logger.info("Controller: destroy.");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.destroyPool();
        connectionPool.deregisterDriver();
        super.destroy();
    }
}