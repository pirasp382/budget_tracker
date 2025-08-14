package org.example.services.validation;

import lombok.experimental.UtilityClass;
import org.example.dto.Message;
import org.example.dto.TransactionDTO;
import org.example.entity.Account;
import org.example.enums.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class TransactionValidation {

  public List<Message> valdiate(final TransactionDTO transactionDTO, final Account account) {
    final List<Message> errorlist = new ArrayList<>();
    if (transactionDTO == null) {
      errorlist.add(Message.builder().title("Transaction is null").build());
      return errorlist;
    }
    if (valueIsNull(account)) {
      errorlist.add(Message.builder().title("Account does not exist").build());
      return errorlist;
    }
    if (valueIsNull(transactionDTO.getAmount())) {
      errorlist.add(Message.builder().title("Amount is null").build());
    }
    if (!typeIsValid(transactionDTO.getType())) {
      errorlist.add(Message.builder().title("Type is not valide").build());
    }
    return errorlist;
  }

  private boolean valueIsNull(final Object object) {
    return object == null;
  }

  private boolean typeIsValid(final String type) {
    return Arrays.stream(Type.values()).anyMatch(item -> item.name().equals(type));
  }

}
