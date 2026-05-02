package com.innowise.web.controller.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class SessionListenerImpl implements HttpSessionListener {
  private static final Logger logger = LogManager.getLogger(SessionListenerImpl.class);

  @Override
  public void sessionCreated(HttpSessionEvent se) {
    logger.debug("HTTP session created: {}", se.getSession().getId());
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
    logger.debug("HTTP session destroyed: {}", se.getSession().getId());
  }
}