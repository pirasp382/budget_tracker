package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import lombok.experimental.UtilityClass;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;

import java.util.List;
import java.util.Map;

@UtilityClass
public class TransactionRepository implements PanacheRepository<Transaction> {

  public Transaction getTransactionById(final User user, final long id) {
    return Transaction.find(
            "select t from Transaction t "
                + "join Account a on a = t.account "
                + "join User u on u = a.user "
                + "where u =: userParam and t.id =: transactionParam",
            Map.of("userParam", user, "transactionParam", id))
        .firstResult();
  }

  public List<Transaction> getAllTransactionsByUser(final User user) {
    return Transaction.find(
            "select t from Transaction  t "
                + "join Account a on a = t.account "
                + "join User u on u = a.user "
                + "where u =: userParam",
            Map.of("userParam", user))
        .list();
  }

  public List<Transaction> getAllTransactionsByUserAndAccount(
      final User user, final Account account) {
    return Transaction.find(
            "select t from Transaction t "
                + "join Account a on a = t.account "
                + "join User u on u = a.user "
                + "where u =: userParam and a =: accountParam",
            Map.of("userParam", user, "accountParam", account))
        .list();
  }
}
