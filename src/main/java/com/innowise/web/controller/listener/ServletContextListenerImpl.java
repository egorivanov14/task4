package com.innowise.web.controller.listener;

import com.innowise.web.connection.ConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    logger.info("Initializing context");
    ConnectionPool.getInstance();
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    logger.info("Destroying context");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    connectionPool.destroyPool();
    connectionPool.deregisterDriver();
  }
}