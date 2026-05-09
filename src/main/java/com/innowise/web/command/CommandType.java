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
  GET_GOOD_DETAIL_LIST(new GetGoodDetailListCommand()),
  ADD_GOOD(new AddGoodCommand()),
  GET_GOOD_DTO_LIST_BY_USER(new GetGoodDtoListByUserCommand()),
  DELETE_GOOD(new DeleteGoodCommand()),
  DELETE_GOOD_BY_ADMIN(new DeleteGoodByAdminCommand()),
  GET_AVAILABLE_GOOD_DTO_LIST(new GetAvailableGoodDtoListCommand()),
  ADD_SHOPPING_CART_ITEM(new AddShoppingCartItemCommand()),
  GET_SHOPPING_CART_BY_USER(new GetShoppingCartByUserCommand()),
  REMOVE_ITEM_FROM_SHOPPING_CART(new RemoveItemFromShoppingCartCommand()),
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