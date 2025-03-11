package org.example.services.validation;

import lombok.experimental.UtilityClass;
import org.example.dto.Category;
import org.example.dto.Message;
import org.example.dto.TransactionDTO;
import org.example.entity.Account;
import org.example.enums.Type;
import org.example.util.CategoryStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
    if (errorlist.isEmpty()
        && transactionDTO.getCategory() != null
        && !categoryExists(transactionDTO.getCategory(), account.getUser().getId())) {
      errorlist.add(Message.builder().title("Category does not exists").build());
    }
    return errorlist;
  }

  private boolean valueIsNull(final Object object) {
    return object == null;
  }

  private boolean typeIsValid(final String type) {
    return Arrays.stream(Type.values()).anyMatch(item -> item.name().equals(type));
  }

  public boolean categoryExists(final String category, final Long userId) {
    final List<Category> categories = CategoryStorage.loadCategories(userId);
    return categories.stream().anyMatch(item -> item.getTitle().equals(category));
  }
}
