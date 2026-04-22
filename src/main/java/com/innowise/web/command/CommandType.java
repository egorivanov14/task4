package com.innowise.web.command;

import com.innowise.web.command.impl.LoginCommand;
import com.innowise.web.command.impl.LogoutCommand;
import com.innowise.web.command.impl.RegisterCommand;

public enum CommandType {
  REGISTER(new RegisterCommand()),
  LOGOUT(new LogoutCommand()),
  LOGIN(new LoginCommand());

  private final Command command;

  CommandType(Command command) {
    this.command = command;
  }

  public static Command defineCommand(String commandStr) {
    CommandType currentType = CommandType.valueOf(commandStr.toUpperCase());
    return currentType.command;
  }
}