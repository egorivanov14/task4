package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.servise.UserService;
import com.innowise.web.servise.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class RegisterCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        UserService userService = UserServiceImpl.getInstance();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String page;
        if (userService.register(username, password)) { // todo
            request.setAttribute("username", username);
            page = "pages/main.jsp";
        } else {
            request.setAttribute("error_msg", "failed to register");
            page = "index.jsp";
        }

        return page;
    }
}