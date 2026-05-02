package com.innowise.web.command;

import com.innowise.web.command.impl.*;

public enum CommandType {
  REGISTER(new RegisterCommand()),
  LOGOUT(new LogoutCommand()),
  LOGIN(new LoginCommand()),
  EDIT_ROLE(new EditRoleCommand()),
  GET_USER_LIST(new GetUserListCommand()),
  GET_PROFILE(new GetProfileCommand()),
  DELETE_CURRENT_USER(new DeleteCurrentUserCommand()),
  DELETE_USER_BY_ADMIN(new DeleteUserByAdminCommand()),
  RETURN_TO_PREVIOUS_PAGE(new ReturnToPreviousPageCommand()),
  GET_GOOD_LIST(new GetGoodListCommand()),
  ADD_GOOD(new AddGoodCommand()),
  GET_GOOD_DTO_LIST_BY_USER(new GetGoodDtoListByUserCommand()),
  DELETE_GOOD_BY_ID(new DeleteGoodByIdCommand()),
  UPDATE_USERNAME(new UpdateUsernameCommand());

  private final Command command;

  CommandType(Command command) {
    this.command = command;
  }

  public static Command defineCommand(String commandStr) {
    CommandType currentType = CommandType.valueOf(commandStr.toUpperCase());
    return currentType.command;
  }
}