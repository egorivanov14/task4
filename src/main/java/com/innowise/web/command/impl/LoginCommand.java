package com.innowise.web.command.impl;

import com.innowise.web.command.Command;
import com.innowise.web.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.innowise.web.config.PublicConstants.*;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    @Override
    public String execute(HttpServletRequest request) {
        logger.info("LoginCommand executing.");
        String username = request.getParameter(USER_NAME_PARAMETER);
        String password = request.getParameter(PASSWORD_PARAMETER);
        UserServiceImpl userService = UserServiceImpl.getInstance();
        boolean result = userService.login(username, password);
        String page;
        if(result){
            logger.info("LoginCommand executed successfully.");
            request.setAttribute(USER_NAME_PARAMETER, username);
            page = MAIN_PAGE;
        }else {
            logger.info("LoginCommand failed.");
            request.setAttribute("error_msg", "failed to login");
            page = LOGIN_PAGE;
        }
        return page;
    }
}