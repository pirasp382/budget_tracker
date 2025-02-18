package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.AccountDTO;
import org.example.dto.RegistrationInput;
import org.example.dto.UserOutput;
import org.example.entity.User;
import org.example.util.Encryption;
import org.example.util.Hasher;
import org.example.util.TokenManager;

import java.util.List;

@UtilityClass
public class UserMapper {
  public User mapToUser(final RegistrationInput input) {
    final String hashedPassword = Hasher.hashpassword(input.getPassword());
    return User.builder()
        .username(input.getUsername())
        .password(hashedPassword)
        .email(Encryption.encrypt(input.getEmail(), hashedPassword))
        .hashedEmail(Hasher.hashValue(input.getEmail()))
        .build();
  }

  public UserOutput mapToUserOutput(final User user) {
    return UserOutput.builder()
        .username(user.getUsername())
        .token(TokenManager.createToken(user))
        .build();
  }

  public UserOutput mapToUserOutput(final User user, final List<AccountDTO> accountDTOList) {
    return UserOutput.builder()
        .username(user.getUsername())
        .accountDTOList(accountDTOList)
        .token(TokenManager.createToken(user))
        .build();
  }
}
