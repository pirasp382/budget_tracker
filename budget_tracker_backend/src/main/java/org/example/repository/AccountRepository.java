package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.experimental.UtilityClass;
import org.example.entity.Account;
import org.example.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@UtilityClass
public class AccountRepository implements PanacheRepository<Account> {

  public Account getAccountByIdAndUserId(final Long id, final User user) {
    return Account.find(
            "select a from Account a where a.id =: idParam and a.user =: userParam",
            Map.of("idParam", id, "userParam", user))
        .firstResult();
  }

  public boolean accountExists(final User user, final Long id) {
    return Account.find(
                "select a from Account a where a.id =: idParam and a.user =: userParam",
                Map.of("idParam", id, "userParam", user))
            .firstResult()
        != null;
  }

  public List<Account> getAccountsByUserId(final User user) {
    return Account.find(
            "select a from Account a where a.user =: accountParam", Map.of("accountParam", user))
        .list();
  }

  public void updateAccountAddAmount(final Account account, final BigDecimal amount) {
    Account.update(
        "update Account a set a.balance = a.balance +: amountParam where a =: accountParam",
        Map.of("amountParam", amount, "accountParam", account));
  }

  public void updateAccountSubstractAmount(final Account account, final BigDecimal amount) {
    Account.update(
        "update Account a set a.balance = a.balance -: amountParam where a =: accountParam",
        Map.of("amountParam", amount, "accountParam", account));
  }
}
