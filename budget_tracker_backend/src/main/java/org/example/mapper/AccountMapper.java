package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.dto.AccountDTO;
import org.example.entity.Account;
import org.example.entity.User;

@UtilityClass
public class AccountMapper {

  public static Account mapToAccount(final AccountDTO accountDTO, final User user) {
    return Account.builder()
        .title(accountDTO.getTitle())
        .balance(accountDTO.getBalance())
        .description(accountDTO.getDescription())
        .user(user)
        .build();
  }

  public static AccountDTO mapToAccountDTO(final Account account) {
    return AccountDTO.builder()
        .id(account.getId())
        .title(account.getTitle())
        .balance(account.getBalance())
        .description(account.getDescription())
        .build();
  }
}
