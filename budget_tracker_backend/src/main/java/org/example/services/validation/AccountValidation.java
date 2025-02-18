package org.example.services.validation;

import lombok.experimental.UtilityClass;
import org.example.dto.AccountDTO;
import org.example.dto.Message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class AccountValidation {

  public List<Message> validate(final AccountDTO accountDTO) {
    final List<Message> errorlist = new ArrayList<>();
    if (valueIsNull(accountDTO.getTitle())) {
      errorlist.add(Message.builder().title("Title is null").build());
    }
    if (balanceIsNegative(accountDTO.getBalance())) {
      errorlist.add(Message.builder().title("Balance is negative").build());
    }
    return errorlist;
  }

  private boolean balanceIsNegative(final BigDecimal value) {
    return value.compareTo(BigDecimal.ZERO) < 0;
  }

  private boolean valueIsNull(final String value) {
    return value == null;
  }
}
