package com.innowise.web.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordCoder {
  private static final int BCRYPT_ROUNDS = 12;

  public static String encode(String password) {
    String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
    return BCrypt.hashpw(password, salt);
  }

  public static boolean checkPassword(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }
}