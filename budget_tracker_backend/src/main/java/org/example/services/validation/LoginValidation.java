package org.example.services.validation;

import lombok.experimental.UtilityClass;
import org.example.dto.LoginInput;
import org.example.dto.Message;
import org.example.repository.UserRepository;
import org.example.util.Hasher;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class LoginValidation {

  public static List<Message> validate(final LoginInput input) {
    final List<Message> errorlist = new ArrayList<>();
    if (input == null) {
      errorlist.add(Message.builder().title("input is null").build());
      return errorlist;
    }
    if (valueIsNull(input.getUsername())) {
      errorlist.add(Message.builder().title("username is null").build());
    }
    if (valueIsNull(input.getPassword())) {
      errorlist.add(Message.builder().title("password is null").build());
    }
    if (errorlist.isEmpty() && !usernameExists(input.getUsername())) {
      errorlist.add(Message.builder().title("user does not exists").build());
    }
    if (errorlist.isEmpty() && wrongCredentials(input.getUsername(), input.getPassword())) {
      errorlist.add(Message.builder().title("wrong credentials").build());
    }
    return errorlist;
  }

  private boolean valueIsNull(final String value) {
    return value == null;
  }

  private boolean usernameExists(final String username) {
    return UserRepository.getUserByUsername(username) != null;
  }

  private boolean wrongCredentials(final String username, final String password) {
    final String userPassword = UserRepository.getPasswordByUsername(username);
    return !Hasher.validatePassword(password, userPassword);
  }
}
