package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class LogoutCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);
    @Override
    public String execute(HttpServletRequest request) {
        logger.info("LogoutCommand execute.");
        request.getSession().removeAttribute(USER_NAME_PARAMETER);
        request.getSession().removeAttribute(PASSWORD_PARAMETER);
        return LOGIN_PAGE;
    }
}