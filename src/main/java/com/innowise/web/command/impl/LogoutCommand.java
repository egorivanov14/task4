package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("password");
        return "index.jsp";
    }
}