package org.example.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import lombok.experimental.UtilityClass;
import org.example.dto.CategoryData;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;
import org.example.enums.Time;
import org.example.enums.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@UtilityClass
public class TransactionRepository implements PanacheRepository<Transaction> {

  public Transaction getTransactionById(final User user, final long id) {
    return Transaction.find(
            "select t from Transaction t join Account a on a = t.account where a.user =: userParam and t.id =: transactionParam",
            Map.of("userParam", user, "transactionParam", id))
        .firstResult();
  }

  public List<Transaction> getAllTransactionsByUser(
      final User user, final String sortValue, final boolean direction, final Account account) {

    String query =
        "select t from Transaction t join Account a on a = t.account where a.user =: userParam";
    final Map<String, Object> params = new HashMap<>();
    params.put("userParam", user);

    if (account != null) {
      query += " and t.account = :accountParam";
      params.put("accountParam", account);
    }

    return Transaction.find(
            query,
            Sort.by(sortValue, getSortDirection(direction))
                .and("createdDate", getSortDirection(direction)),
            params)
        .list();
  }

  public List<Transaction> getAllTransactionsByUser(final User user, final Account account) {
    String query =
        "select t from Transaction t join Account a on a = t.account where a.user =: userParam";
    HashMap<String, Object> params = new HashMap<>();
    params.put("userParam", user);
    if (account != null) {
      query += " and t.account =: accountParam";
      params.put("accountParam", account);
    }
    return Transaction.find(
            query,
            Sort.by("date", getSortDirection(false)).and("createdDate", getSortDirection(false)),
            params)
        .list();
  }

  public boolean transactionExists(final User user, final Long transactionId) {
    return Transaction.find(
            "select t from Transaction  t join Account a on t.account  where a.user =: userParam and t.id =: transactionParam",
            Map.of("userParam", user, "transactionParam", transactionId))
        != null;
  }

  public BigDecimal getTransactionsTypeSum(final User user, final Type type) {
    return Transaction.find(
            "select abs(sum(t.amount)) from Transaction t join Account a on t.account = a where a.user =: userParam and t.type =: typeParam",
            Map.of("userParam", user, "typeParam", type))
        .project(BigDecimal.class)
        .firstResult();
  }

  public List<Map<LocalDate, BigDecimal>> getAllTransactionsOfType(
      final User user, final Type type, final Account account, final Time time) {
    String query =
        "select t.date, abs(sum(t.amount)) from Transaction t join Account a on a = t.account where a.user =: userParam and t.type =: typeParam";
    HashMap<String, Object> param = new HashMap<>();
    param.put("userParam", user);
    param.put("typeParam", type);
    if (account != null) {
      query += " and t.account =: accountParam";
      param.put("accountParam", account);
    }
    if (time != null) {
      switch (time) {
        case DAY -> query += " group by day(t.date) , year (t.date)";
        case WEEK -> query += " group by week(t.date) , year (t.date)";
        case MONTH -> query += " group by month(t.date) , year (t.date)";
        case YEAR -> query += " group by year (t.date)";
      }
    } else {
      query += " group by t.date";
    }
    return Transaction.find(query, param).project(Map.class).list();
  }

  public List<Map<LocalDate, BigDecimal>> getAllTransactionSumsByCategory(
      final User user, final Account account) {
    String query =
        "select t.category, abs(sum(t.amount)) from Transaction t join Account a on a = t.account where a.user =: userParam";
    HashMap<String, Object> params = new HashMap<>();
    params.put("userParam", user);

    if (account != null) {
      query += " and t.account =: accountParam";
      params.put("accountParam", account);
    }
    query += " group by t.category";

    return Transaction.find(query, params).project(Map.class).list();
  }

  private Sort.Direction getSortDirection(final boolean direction) {
    return direction ? Sort.Direction.Ascending : Sort.Direction.Descending;
  }
}
