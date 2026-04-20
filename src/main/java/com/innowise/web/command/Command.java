package com.innowise.web.command;

import com.innowise.web.controller.Router;
import com.innowise.web.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface Command {
    Router execute(HttpServletRequest request) throws CommandException;
}